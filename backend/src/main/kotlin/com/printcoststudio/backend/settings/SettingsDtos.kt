package com.printcoststudio.backend.settings

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant

data class SettingsRequest(
    @field:NotBlank @field:Size(min = 3, max = 3)
    val currency: String,
    @field:DecimalMin("0")
    val electricityRatePerKwh: BigDecimal,
    @field:DecimalMin("0")
    val printerPowerConsumptionWatts: BigDecimal,
    @field:DecimalMin("0")
    val printerPurchasePrice: BigDecimal,
    @field:DecimalMin(value = "0.01")
    val printerLifespanHours: BigDecimal,
    @field:DecimalMin("0")
    val defaultMarginPercentage: BigDecimal,
    @field:DecimalMin("0")
    val laborHourlyRate: BigDecimal,
)

data class SettingsResponse(
    val currency: String,
    val electricityRatePerKwh: BigDecimal,
    val printerPowerConsumptionWatts: BigDecimal,
    val printerPurchasePrice: BigDecimal,
    val printerLifespanHours: BigDecimal,
    val defaultMarginPercentage: BigDecimal,
    val laborHourlyRate: BigDecimal,
    val updatedAt: Instant,
) {
    companion object {
        fun from(settings: Settings) =
            SettingsResponse(
                currency = settings.currency,
                electricityRatePerKwh = settings.electricityRatePerKwh,
                printerPowerConsumptionWatts = settings.printerPowerConsumptionWatts,
                printerPurchasePrice = settings.printerPurchasePrice,
                printerLifespanHours = settings.printerLifespanHours,
                defaultMarginPercentage = settings.defaultMarginPercentage,
                laborHourlyRate = settings.laborHourlyRate,
                updatedAt = settings.updatedAt,
            )
    }
}
