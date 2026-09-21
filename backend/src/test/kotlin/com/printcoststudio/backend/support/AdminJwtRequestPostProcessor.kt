package com.printcoststudio.backend.support

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.request.RequestPostProcessor
import java.util.UUID

/** Simulates a valid ADMIN access token for [userId] without exercising the real login endpoint. */
fun asAdmin(userId: UUID): RequestPostProcessor =
    jwt()
        .authorities(SimpleGrantedAuthority("ROLE_ADMIN"))
        .jwt { it.subject(userId.toString()) }
