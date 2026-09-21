package com.printcoststudio.backend.electricity

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class ElectricityRecordRequest(
    @field:NotNull
    val periodStart: LocalDate,
    @field:NotNull
    val periodEnd: LocalDate,
    @field:DecimalMin(value = "0.001")
    val kwhConsumed: BigDecimal,
    @field:DecimalMin(value = "0.01")
    val totalBillAmount: BigDecimal,
    val notes: String? = null,
)

data class ElectricityRecordResponse(
    val id: UUID,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val kwhConsumed: BigDecimal,
    val totalBillAmount: BigDecimal,
    val computedRatePerKwh: BigDecimal,
    val notes: String?,
    val createdAt: Instant,
) {
    companion object {
        fun from(record: ElectricityConsumptionRecord) =
            ElectricityRecordResponse(
                id = record.id!!,
                periodStart = record.periodStart,
                periodEnd = record.periodEnd,
                kwhConsumed = record.kwhConsumed,
                totalBillAmount = record.totalBillAmount,
                computedRatePerKwh = record.totalBillAmount.divide(record.kwhConsumed, 4, RoundingMode.HALF_UP),
                notes = record.notes,
                createdAt = record.createdAt,
            )
    }
}
