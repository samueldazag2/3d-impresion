package com.printcoststudio.backend.material

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class MaterialRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val type: String,
    @field:NotBlank
    val color: String,
    @field:DecimalMin(value = "0.01")
    val pricePerKg: BigDecimal,
    @field:DecimalMin(value = "0")
    val currentStockGrams: BigDecimal,
    val densityGramsPerCm3: BigDecimal? = null,
)

data class MaterialResponse(
    val id: UUID,
    val name: String,
    val type: String,
    val color: String,
    val pricePerKg: BigDecimal,
    val currentStockGrams: BigDecimal,
    val densityGramsPerCm3: BigDecimal?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun from(material: Material) =
            MaterialResponse(
                id = material.id!!,
                name = material.name,
                type = material.type,
                color = material.color,
                pricePerKg = material.pricePerKg,
                currentStockGrams = material.currentStockGrams,
                densityGramsPerCm3 = material.densityGramsPerCm3,
                createdAt = material.createdAt,
                updatedAt = material.updatedAt,
            )
    }
}
