package com.printcoststudio.backend.material

import com.printcoststudio.backend.auth.Role
import com.printcoststudio.backend.auth.User
import com.printcoststudio.backend.auth.UserRepository
import com.printcoststudio.backend.support.asAdmin
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Transactional
class MaterialControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private fun adminPostProcessor() =
        asAdmin(
            userRepository
                .save(
                    User(
                        email = "material-tester@printcoststudio.test",
                        passwordHash = passwordEncoder.encode("irrelevant")!!,
                        role = Role.ADMIN,
                    ),
                ).id!!,
        )

    @Test
    fun `full create-read-update-delete lifecycle for a material`() {
        val admin = adminPostProcessor()
        val requestBody =
            objectMapper.writeValueAsString(
                MaterialRequest(
                    name = "PLA Naranja",
                    type = "PLA",
                    color = "Naranja",
                    pricePerKg = java.math.BigDecimal("25.00"),
                    currentStockGrams = java.math.BigDecimal("1000"),
                ),
            )

        val createResult =
            mockMvc
                .perform(post("/api/materials").with(admin).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.name").value("PLA Naranja"))
                .andReturn()

        val createdId = objectMapper.readTree(createResult.response.contentAsByteArray).get("id").asString()

        mockMvc
            .perform(get("/api/materials/$createdId").with(admin))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.pricePerKg").value(25.00))

        mockMvc
            .perform(get("/api/materials").with(admin))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalElements").value(1))

        mockMvc
            .perform(delete("/api/materials/$createdId").with(admin))
            .andExpect(status().isNoContent)

        mockMvc
            .perform(get("/api/materials/$createdId").with(admin))
            .andExpect(status().isNotFound)
    }
}
