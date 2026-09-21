package com.printcoststudio.backend.auth

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/**
 * There is no signup flow in Fase 1 - the single ADMIN account is
 * provisioned from environment variables on first boot, if it does not
 * already exist. Safe to leave configured permanently: it is a no-op once
 * the admin user has been created.
 */
@Component
class AdminSeeder(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${app.security.admin.email:}") private val adminEmail: String,
    @Value("\${app.security.admin.password:}") private val adminPassword: String,
) : ApplicationRunner {
    private val log = LoggerFactory.getLogger(AdminSeeder::class.java)

    override fun run(args: ApplicationArguments) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.warn("ADMIN_EMAIL/ADMIN_PASSWORD not set - skipping admin seed")
            return
        }
        if (userRepository.existsByEmail(adminEmail)) {
            return
        }

        userRepository.save(
            User(
                email = adminEmail,
                passwordHash = passwordEncoder.encode(adminPassword)!!,
                role = Role.ADMIN,
            ),
        )
        log.info("Seeded initial ADMIN user: {}", adminEmail)
    }
}
