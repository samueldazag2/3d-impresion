package com.printcoststudio.backend.quote

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class QuoteCostCalculatorTest {
    private fun baseInput(
        materialGramsUsed: String = "50",
        materialPricePerKg: String = "25.00",
        printTimeHours: String = "3",
        printerPowerConsumptionWatts: String = "200",
        electricityRatePerKwh: String = "0.60",
        printerPurchasePrice: String = "1500",
        printerLifespanHours: String = "5000",
        laborHours: String = "1",
        laborHourlyRate: String = "8",
        packagingCost: String = "1.50",
        marginPercentage: String = "30",
    ) = CostCalculationInput(
        materialGramsUsed = BigDecimal(materialGramsUsed),
        materialPricePerKg = BigDecimal(materialPricePerKg),
        printTimeHours = BigDecimal(printTimeHours),
        printerPowerConsumptionWatts = BigDecimal(printerPowerConsumptionWatts),
        electricityRatePerKwh = BigDecimal(electricityRatePerKwh),
        printerPurchasePrice = BigDecimal(printerPurchasePrice),
        printerLifespanHours = BigDecimal(printerLifespanHours),
        laborHours = BigDecimal(laborHours),
        laborHourlyRate = BigDecimal(laborHourlyRate),
        packagingCost = BigDecimal(packagingCost),
        marginPercentage = BigDecimal(marginPercentage),
    )

    @Test
    fun `calculates every line of the breakdown for a standard quote`() {
        val result = QuoteCostCalculator.calculate(baseInput())

        // materialCost = (50 / 1000) * 25.00 = 1.25
        assertEquals(BigDecimal("1.25"), result.materialCost)
        // electricityCost = (200 / 1000) * 3 * 0.60 = 0.36
        assertEquals(BigDecimal("0.36"), result.electricityCost)
        // printerWearCost = (1500 / 5000) * 3 = 0.90
        assertEquals(BigDecimal("0.90"), result.printerWearCost)
        // laborCost = 1 * 8 = 8.00
        assertEquals(BigDecimal("8.00"), result.laborCost)
        assertEquals(BigDecimal("1.50"), result.packagingCost)
        // totalCost = 1.25 + 0.36 + 0.90 + 8.00 + 1.50 = 12.01
        assertEquals(BigDecimal("12.01"), result.totalCost)
        // suggestedPrice = 12.01 * 1.30 = 15.613 -> rounds to 15.61
        assertEquals(BigDecimal("15.61"), result.suggestedPrice)
    }

    @Test
    fun `zero margin makes suggested price equal to total cost`() {
        val result = QuoteCostCalculator.calculate(baseInput(marginPercentage = "0"))

        assertEquals(result.totalCost, result.suggestedPrice)
    }

    @Test
    fun `zero packaging cost does not break the total`() {
        val result = QuoteCostCalculator.calculate(baseInput(packagingCost = "0"))

        assertEquals(BigDecimal("0.00"), result.packagingCost)
        assertEquals(
            result.materialCost + result.electricityCost + result.printerWearCost + result.laborCost,
            result.totalCost,
        )
    }

    @Test
    fun `rejects a printer lifespan of zero instead of dividing by it`() {
        assertFailsWith<IllegalArgumentException> {
            QuoteCostCalculator.calculate(baseInput(printerLifespanHours = "0"))
        }
    }

    @Test
    fun `every breakdown amount is rounded to two decimals`() {
        val result =
            QuoteCostCalculator.calculate(
                baseInput(
                    materialGramsUsed = "37",
                    materialPricePerKg = "19.99",
                    printTimeHours = "2.75",
                    printerPowerConsumptionWatts = "180",
                    electricityRatePerKwh = "0.4321",
                    printerPurchasePrice = "987.65",
                    printerLifespanHours = "3333",
                    laborHours = "0.5",
                    laborHourlyRate = "7.25",
                    packagingCost = "0.99",
                    marginPercentage = "27.5",
                ),
            )

        listOf(
            result.materialCost,
            result.electricityCost,
            result.printerWearCost,
            result.laborCost,
            result.packagingCost,
            result.totalCost,
            result.suggestedPrice,
        ).forEach { amount -> assertEquals(2, amount.scale(), "expected 2 decimal places for $amount") }
    }

    @ParameterizedTest
    @CsvSource(
        "0, 12.01, 12.01",
        "10, 12.01, 13.21",
        "100, 12.01, 24.02",
    )
    fun `suggested price scales linearly with margin percentage`(
        margin: String,
        expectedTotal: String,
        expectedSuggested: String,
    ) {
        val result = QuoteCostCalculator.calculate(baseInput(marginPercentage = margin))

        assertEquals(BigDecimal(expectedTotal), result.totalCost)
        assertEquals(BigDecimal(expectedSuggested), result.suggestedPrice)
    }
}
