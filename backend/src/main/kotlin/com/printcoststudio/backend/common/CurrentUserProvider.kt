package com.printcoststudio.backend.common

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CurrentUserProvider {
    fun currentUserId(): UUID {
        val jwt = SecurityContextHolder.getContext().authentication!!.principal as Jwt
        return UUID.fromString(jwt.subject)
    }
}
