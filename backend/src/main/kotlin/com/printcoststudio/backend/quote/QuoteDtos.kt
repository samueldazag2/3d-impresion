package com.printcoststudio.backend.quote

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class QuoteRequest(
    val clientId: UUID? = null,
    @field:NotNull
    val materialId: UUID,
    @field:NotBlank
    val title: String,
    @field:DecimalMin(value = "0.01")
    val materialGramsUsed: BigDecimal,
    @field:DecimalMin(value = "0.01")
    val printTimeHours: BigDecimal,
    @field:DecimalMin(value = "0")
    val laborHours: BigDecimal,
    @field:DecimalMin(value = "0")
    val packagingCost: BigDecimal,
    /** Falls back to Settings.defaultMarginPercentage when omitted. */
    val marginPercentage: BigDecimal? = null,
)

data class QuoteStatusUpdateRequest(
    @field:NotNull
    val status: QuoteStatus,
)

data class QuoteResponse(
    val id: UUID,
    val clientId: UUID?,
    val materialId: UUID,
    val title: String,
    val materialGramsUsed: BigDecimal,
    val printTimeHours: BigDecimal,
    val laborHours: BigDecimal,
    val packagingCost: BigDecimal,
    val materialCost: BigDecimal,
    val electricityCost: BigDecimal,
    val printerWearCost: BigDecimal,
    val laborCost: BigDecimal,
    val marginPercentage: BigDecimal,
    val totalCost: BigDecimal,
    val suggestedPrice: BigDecimal,
    val currency: String,
    val status: QuoteStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun from(quote: Quote) =
            QuoteResponse(
                id = quote.id!!,
                clientId = quote.client?.id,
                materialId = quote.material.id!!,
                title = quote.title,
                materialGramsUsed = quote.materialGramsUsed,
                printTimeHours = quote.printTimeHours,
                laborHours = quote.laborHours,
                packagingCost = quote.packagingCost,
                materialCost = quote.materialCost,
                electricityCost = quote.electricityCost,
                printerWearCost = quote.printerWearCost,
                laborCost = quote.laborCost,
                marginPercentage = quote.marginPercentage,
                totalCost = quote.totalCost,
                suggestedPrice = quote.suggestedPrice,
                currency = quote.currency,
                status = quote.status,
                createdAt = quote.createdAt,
                updatedAt = quote.updatedAt,
            )
    }
}
