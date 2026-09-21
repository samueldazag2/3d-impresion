package com.printcoststudio.backend.auth

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.printcoststudio.backend.common.InvalidTokenException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.text.ParseException
import java.time.Instant
import java.util.Date

@Service
class JwtService(
    @Value("\${app.security.jwt.secret}") secret: String,
) {
    private val signer = MACSigner(secret.toByteArray())
    private val verifier = MACVerifier(secret.toByteArray())

    fun issueAccessToken(user: User): String = issue(user, ACCESS_TTL_SECONDS, TYPE_ACCESS)

    fun issueRefreshToken(user: User): String = issue(user, REFRESH_TTL_SECONDS, TYPE_REFRESH)

    /** Validates signature, type and expiry; throws [InvalidTokenException] otherwise. */
    fun parseRefreshToken(token: String): JWTClaimsSet {
        val jwt =
            try {
                SignedJWT.parse(token)
            } catch (e: ParseException) {
                throw InvalidTokenException("Malformed token")
            }
        if (!jwt.verify(verifier)) throw InvalidTokenException("Invalid token signature")

        val claims = jwt.jwtClaimsSet
        if (claims.getStringClaim(CLAIM_TYPE) != TYPE_REFRESH) throw InvalidTokenException("Not a refresh token")
        if (claims.expirationTime == null || claims.expirationTime.before(Date())) {
            throw InvalidTokenException("Refresh token expired")
        }
        return claims
    }

    private fun issue(
        user: User,
        ttlSeconds: Long,
        type: String,
    ): String {
        val now = Instant.now()
        val claims =
            JWTClaimsSet
                .Builder()
                .subject(user.id.toString())
                .claim("email", user.email)
                .claim("role", user.role.name)
                .claim(CLAIM_TYPE, type)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(ttlSeconds)))
                .build()
        val jwt = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claims)
        jwt.sign(signer)
        return jwt.serialize()
    }

    companion object {
        private const val CLAIM_TYPE = "typ"
        const val TYPE_ACCESS = "access"
        const val TYPE_REFRESH = "refresh"
        private const val ACCESS_TTL_SECONDS = 15 * 60L
        private const val REFRESH_TTL_SECONDS = 7 * 24 * 60 * 60L
    }
}
