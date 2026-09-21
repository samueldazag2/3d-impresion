package com.printcoststudio.backend.electricity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "electricity_consumption_records")
class ElectricityConsumptionRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @Column(name = "period_start", nullable = false)
    var periodStart: LocalDate,
    @Column(name = "period_end", nullable = false)
    var periodEnd: LocalDate,
    @Column(name = "kwh_consumed", nullable = false, precision = 12, scale = 3)
    var kwhConsumed: BigDecimal,
    @Column(name = "total_bill_amount", nullable = false, precision = 12, scale = 2)
    var totalBillAmount: BigDecimal,
    @Column(columnDefinition = "TEXT")
    var notes: String? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),
)
