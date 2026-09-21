package com.printcoststudio.backend.quote

import com.printcoststudio.backend.auth.Role
import com.printcoststudio.backend.auth.User
import com.printcoststudio.backend.auth.UserRepository
import com.printcoststudio.backend.material.Material
import com.printcoststudio.backend.material.MaterialRepository
import com.printcoststudio.backend.support.asAdmin
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
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Transactional
class QuoteControllerIT {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var materialRepository: MaterialRepository

    @Test
    fun `creating a quote computes and persists the cost breakdown via the settings defaults`() {
        val admin =
            asAdmin(
                userRepository
                    .save(
                        User(
                            email = "quote-tester@printcoststudio.test",
                            passwordHash = passwordEncoder.encode("irrelevant")!!,
                            role = Role.ADMIN,
                        ),
                    ).id!!,
            )
        val material =
            materialRepository.save(
                Material(
                    name = "PLA Naranja",
                    type = "PLA",
                    color = "Naranja",
                    pricePerKg = BigDecimal("25.00"),
                    currentStockGrams = BigDecimal("1000"),
                ),
            )

        val requestBody =
            objectMapper.writeValueAsString(
                QuoteRequest(
                    materialId = material.id!!,
                    title = "Llavero personalizado",
                    materialGramsUsed = BigDecimal("50"),
                    printTimeHours = BigDecimal("3"),
                    laborHours = BigDecimal("1"),
                    packagingCost = BigDecimal("1.50"),
                ),
            )

        // Default settings (seeded on first access): rate 0.60/kWh, 200W, purchase 1500,
        // lifespan 5000h, labor 8/h, margin 30% -> same numbers as QuoteCostCalculatorTest.
        mockMvc
            .perform(post("/api/quotes").with(admin).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.materialCost").value(1.25))
            .andExpect(jsonPath("$.electricityCost").value(0.36))
            .andExpect(jsonPath("$.printerWearCost").value(0.90))
            .andExpect(jsonPath("$.laborCost").value(8.00))
            .andExpect(jsonPath("$.totalCost").value(12.01))
            .andExpect(jsonPath("$.suggestedPrice").value(15.61))
            .andExpect(jsonPath("$.status").value("PENDING"))
    }

    @Test
    fun `creating a quote for a material that does not exist returns 404`() {
        val admin =
            asAdmin(
                userRepository
                    .save(
                        User(
                            email = "quote-tester-2@printcoststudio.test",
                            passwordHash = passwordEncoder.encode("irrelevant")!!,
                            role = Role.ADMIN,
                        ),
                    ).id!!,
            )

        val requestBody =
            objectMapper.writeValueAsString(
                QuoteRequest(
                    materialId = java.util.UUID.randomUUID(),
                    title = "Pieza inexistente",
                    materialGramsUsed = BigDecimal("10"),
                    printTimeHours = BigDecimal("1"),
                    laborHours = BigDecimal("0"),
                    packagingCost = BigDecimal("0"),
                ),
            )

        mockMvc
            .perform(post("/api/quotes").with(admin).contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isNotFound)
    }
}
