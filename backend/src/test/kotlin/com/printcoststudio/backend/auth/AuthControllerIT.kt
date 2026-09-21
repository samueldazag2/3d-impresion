package com.printcoststudio.backend.auth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Transactional
class AuthControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `login with valid credentials returns an access and refresh token`() {
        userRepository.save(
            User(
                email = "quality-admin@printcoststudio.test",
                passwordHash = passwordEncoder.encode("correct-horse-battery-staple")!!,
                role = Role.ADMIN,
            ),
        )

        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            LoginRequest(email = "quality-admin@printcoststudio.test", password = "correct-horse-battery-staple"),
                        ),
                    ),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").isNotEmpty)
            .andExpect(jsonPath("$.refreshToken").isNotEmpty)
            .andExpect(jsonPath("$.role").value("ADMIN"))
    }

    @Test
    fun `login with wrong password is rejected`() {
        userRepository.save(
            User(
                email = "wrong-pass@printcoststudio.test",
                passwordHash = passwordEncoder.encode("correct-horse-battery-staple")!!,
                role = Role.ADMIN,
            ),
        )

        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            LoginRequest(email = "wrong-pass@printcoststudio.test", password = "not-the-password"),
                        ),
                    ),
            ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `login for an email that does not exist is rejected`() {
        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            LoginRequest(email = "nobody@printcoststudio.test", password = "whatever123"),
                        ),
                    ),
            ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `protected endpoints reject requests without a token`() {
        mockMvc
            .perform(post("/api/materials"))
            .andExpect(status().isUnauthorized)
    }
}
