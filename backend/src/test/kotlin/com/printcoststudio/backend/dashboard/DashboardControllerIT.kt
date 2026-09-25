package com.printcoststudio.backend.dashboard

import com.printcoststudio.backend.auth.Role
import com.printcoststudio.backend.auth.User
import com.printcoststudio.backend.auth.UserRepository
import com.printcoststudio.backend.material.Material
import com.printcoststudio.backend.material.MaterialRepository
import com.printcoststudio.backend.quote.QuoteRequest
import com.printcoststudio.backend.support.asAdmin
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
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
class DashboardControllerIT {
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
    fun `summary splits pipeline from delivered revenue and flags low stock`() {
        val admin =
            asAdmin(
                userRepository
                    .save(
                        User(
                            email = "dashboard-tester@printcoststudio.test",
                            passwordHash = passwordEncoder.encode("irrelevant")!!,
                            role = Role.ADMIN,
                        ),
                    ).id!!,
            )
        val material =
            materialRepository.save(
                Material(
                    name = "PETG Azul",
                    type = "PETG",
                    color = "Azul",
                    pricePerKg = BigDecimal("25.00"),
                    currentStockGrams = BigDecimal("100"),
                ),
            )

        fun createQuote(title: String): String {
            val body =
                objectMapper.writeValueAsString(
                    QuoteRequest(
                        materialId = material.id!!,
                        title = title,
                        materialGramsUsed = BigDecimal("50"),
                        printTimeHours = BigDecimal("3"),
                        laborHours = BigDecimal("1"),
                        packagingCost = BigDecimal("1.50"),
                    ),
                )
            val result =
                mockMvc
                    .perform(post("/api/quotes").with(admin).contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isCreated)
                    .andReturn()
            return objectMapper.readTree(result.response.contentAsByteArray).get("id").asString()
        }

        createQuote("Pendiente")
        val deliveredId = createQuote("Entregada")
        mockMvc
            .perform(
                patch("/api/quotes/$deliveredId/status")
                    .with(admin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"status":"DELIVERED"}"""),
            ).andExpect(status().isOk)

        // Each quote costs 12.01 and is suggested at 15.61 with the default settings.
        mockMvc
            .perform(get("/api/dashboard").with(admin))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.quotesByStatus.PENDING").value(1))
            .andExpect(jsonPath("$.quotesByStatus.DELIVERED").value(1))
            .andExpect(jsonPath("$.pipelineValue").value(15.61))
            .andExpect(jsonPath("$.deliveredRevenue").value(15.61))
            .andExpect(jsonPath("$.deliveredCost").value(12.01))
            .andExpect(jsonPath("$.deliveredProfit").value(3.60))
            .andExpect(jsonPath("$.lowStockMaterials[0].name").value("PETG Azul"))
    }
}
