package com.printcoststudio.backend.settings

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * Single-row table: PrintCost Studio has one admin workspace in Fase 1, so
 * there is exactly one settings record, identified by [SINGLETON_ID].
 */
@Entity
@Table(name = "settings")
class Settings(
    @Id
    val id: UUID = SINGLETON_ID,
    @Column(nullable = false, length = 3)
    var currency: String,
    @Column(name = "electricity_rate_per_kwh", nullable = false, precision = 12, scale = 4)
    var electricityRatePerKwh: BigDecimal,
    @Column(name = "printer_power_consumption_watts", nullable = false, precision = 10, scale = 2)
    var printerPowerConsumptionWatts: BigDecimal,
    @Column(name = "printer_purchase_price", nullable = false, precision = 12, scale = 2)
    var printerPurchasePrice: BigDecimal,
    @Column(name = "printer_lifespan_hours", nullable = false, precision = 10, scale = 2)
    var printerLifespanHours: BigDecimal,
    @Column(name = "default_margin_percentage", nullable = false, precision = 5, scale = 2)
    var defaultMarginPercentage: BigDecimal,
    @Column(name = "labor_hourly_rate", nullable = false, precision = 12, scale = 2)
    var laborHourlyRate: BigDecimal,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
) {
    companion object {
        val SINGLETON_ID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

        fun default() =
            Settings(
                currency = "COP",
                electricityRatePerKwh = BigDecimal("0.60"),
                printerPowerConsumptionWatts = BigDecimal("200"),
                printerPurchasePrice = BigDecimal("1500"),
                printerLifespanHours = BigDecimal("5000"),
                defaultMarginPercentage = BigDecimal("30"),
                laborHourlyRate = BigDecimal("8"),
            )
    }
}
