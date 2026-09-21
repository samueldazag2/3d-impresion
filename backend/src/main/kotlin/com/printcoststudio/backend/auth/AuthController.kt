package com.printcoststudio.backend.auth

import com.printcoststudio.backend.audit.AuditService
import com.printcoststudio.backend.common.InvalidCredentialsException
import jakarta.validation.Valid
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val auditService: AuditService,
) {
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): TokenResponse {
        val user =
            userRepository
                .findByEmail(request.email)
                .orElseThrow { InvalidCredentialsException("Invalid email or password") }
        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw InvalidCredentialsException("Invalid email or password")
        }

        auditService.record(userId = user.id!!, action = "LOGIN")

        return TokenResponse(
            accessToken = jwtService.issueAccessToken(user),
            refreshToken = jwtService.issueRefreshToken(user),
            email = user.email,
            role = user.role,
        )
    }

    @PostMapping("/refresh")
    fun refresh(
        @Valid @RequestBody request: RefreshRequest,
    ): AccessTokenResponse {
        val claims = jwtService.parseRefreshToken(request.refreshToken)
        val userId = UUID.fromString(claims.subject)
        val user =
            userRepository
                .findById(userId)
                .orElseThrow { InvalidCredentialsException("Invalid refresh token") }

        return AccessTokenResponse(jwtService.issueAccessToken(user))
    }
}
