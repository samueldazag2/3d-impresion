package com.printcoststudio.backend.config

import com.printcoststudio.backend.auth.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
import org.springframework.web.cors.CorsConfigurationSource
import javax.crypto.spec.SecretKeySpec

@Configuration
class SecurityConfig(
    @Value("\${app.security.jwt.secret}") private val jwtSecret: String,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val key = SecretKeySpec(jwtSecret.toByteArray(), "HmacSHA256")
        val decoder = NimbusJwtDecoder.withSecretKey(key).build()

        val accessTokenOnly =
            OAuth2TokenValidator<Jwt> { jwt ->
                if (jwt.claims["typ"] == JwtService.TYPE_ACCESS) {
                    OAuth2TokenValidatorResult.success()
                } else {
                    OAuth2TokenValidatorResult.failure(OAuth2Error("invalid_token", "Not an access token", null))
                }
            }
        decoder.setJwtValidator(JwtValidators.createDefaultWithValidators(accessTokenOnly))
        return decoder
    }

    @Bean
    fun jwtAuthenticationConverter(): Converter<Jwt, AbstractAuthenticationToken> =
        JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter { jwt ->
                val role = jwt.getClaimAsString("role") ?: return@setJwtGrantedAuthoritiesConverter emptyList()
                listOf(SimpleGrantedAuthority("ROLE_$role"))
            }
        }

    @Bean
    fun filterChain(
        http: HttpSecurity,
        corsConfigurationSource: CorsConfigurationSource,
        jwtDecoder: JwtDecoder,
        jwtAuthenticationConverter: Converter<Jwt, AbstractAuthenticationToken>,
    ): SecurityFilterChain {
        http {
            cors { configurationSource = corsConfigurationSource }
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                authorize("/actuator/health", permitAll)
                authorize("/api/auth/login", permitAll)
                authorize("/api/auth/refresh", permitAll)
                authorize(anyRequest, hasRole("ADMIN"))
            }
            oauth2ResourceServer {
                jwt {
                    this.jwtDecoder = jwtDecoder
                    this.jwtAuthenticationConverter = jwtAuthenticationConverter
                }
            }
            headers {
                frameOptions { deny = true }
                contentTypeOptions { }
                referrerPolicy { policy = ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN }
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(LoginRateLimitFilter())
        }
        return http.build()
    }
}
