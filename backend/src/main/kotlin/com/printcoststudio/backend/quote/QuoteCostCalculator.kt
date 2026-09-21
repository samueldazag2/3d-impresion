package com.printcoststudio.backend.quote

import java.math.BigDecimal
import java.math.RoundingMode

data class CostCalculationInput(
    val materialGramsUsed: BigDecimal,
    val materialPricePerKg: BigDecimal,
    val printTimeHours: BigDecimal,
    val printerPowerConsumptionWatts: BigDecimal,
    val electricityRatePerKwh: BigDecimal,
    val printerPurchasePrice: BigDecimal,
    val printerLifespanHours: BigDecimal,
    val laborHours: BigDecimal,
    val laborHourlyRate: BigDecimal,
    val packagingCost: BigDecimal,
    val marginPercentage: BigDecimal,
)

data class CostBreakdown(
    val materialCost: BigDecimal,
    val electricityCost: BigDecimal,
    val printerWearCost: BigDecimal,
    val laborCost: BigDecimal,
    val packagingCost: BigDecimal,
    val totalCost: BigDecimal,
    val suggestedPrice: BigDecimal,
)

/**
 * Pure, side-effect-free cost engine. This is the most business-critical
 * piece of PrintCost Studio: a rounding or formula mistake here means the
 * admin quotes a wrong price. Every intermediate amount is rounded to money
 * scale (2 decimals, HALF_UP) before being summed, so the breakdown always
 * adds up to the same total a human would get re-adding the displayed lines.
 */
object QuoteCostCalculator {
    private const val MONEY_SCALE = 2
    private const val INTERMEDIATE_SCALE = 6
    private val ROUNDING = RoundingMode.HALF_UP
    private val GRAMS_PER_KG = BigDecimal(1000)
    private val WATTS_PER_KW = BigDecimal(1000)
    private val HUNDRED = BigDecimal(100)

    fun calculate(input: CostCalculationInput): CostBreakdown {
        require(input.printerLifespanHours > BigDecimal.ZERO) {
            "printerLifespanHours must be greater than zero"
        }

        val materialCost =
            money(
                input.materialGramsUsed
                    .divide(GRAMS_PER_KG, INTERMEDIATE_SCALE, ROUNDING)
                    .multiply(input.materialPricePerKg),
            )
        val electricityCost =
            money(
                input.printerPowerConsumptionWatts
                    .divide(WATTS_PER_KW, INTERMEDIATE_SCALE, ROUNDING)
                    .multiply(input.printTimeHours)
                    .multiply(input.electricityRatePerKwh),
            )
        val printerWearCost =
            money(
                input.printerPurchasePrice
                    .divide(input.printerLifespanHours, INTERMEDIATE_SCALE, ROUNDING)
                    .multiply(input.printTimeHours),
            )
        val laborCost = money(input.laborHours.multiply(input.laborHourlyRate))
        val packagingCost = money(input.packagingCost)

        val totalCost = money(materialCost + electricityCost + printerWearCost + laborCost + packagingCost)

        val marginMultiplier =
            BigDecimal.ONE + input.marginPercentage.divide(HUNDRED, INTERMEDIATE_SCALE, ROUNDING)
        val suggestedPrice = money(totalCost.multiply(marginMultiplier))

        return CostBreakdown(
            materialCost = materialCost,
            electricityCost = electricityCost,
            printerWearCost = printerWearCost,
            laborCost = laborCost,
            packagingCost = packagingCost,
            totalCost = totalCost,
            suggestedPrice = suggestedPrice,
        )
    }

    private fun money(value: BigDecimal): BigDecimal = value.setScale(MONEY_SCALE, ROUNDING)
}
