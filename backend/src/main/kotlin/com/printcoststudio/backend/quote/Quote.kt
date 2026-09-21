package com.printcoststudio.backend.quote

import com.printcoststudio.backend.client.Client
import com.printcoststudio.backend.material.Material
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * Cost fields are a snapshot computed at creation time (see [QuoteCostCalculator]).
 * They intentionally do not recompute from live Settings/Material data, so a
 * later price or rate change never alters a historical quote.
 */
@Entity
@Table(name = "quotes")
class Quote(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    var client: Client? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    var material: Material,
    @Column(nullable = false)
    var title: String,
    @Column(name = "material_grams_used", nullable = false, precision = 10, scale = 2)
    var materialGramsUsed: BigDecimal,
    @Column(name = "print_time_hours", nullable = false, precision = 10, scale = 2)
    var printTimeHours: BigDecimal,
    @Column(name = "labor_hours", nullable = false, precision = 10, scale = 2)
    var laborHours: BigDecimal,
    @Column(name = "packaging_cost", nullable = false, precision = 12, scale = 2)
    var packagingCost: BigDecimal,
    @Column(name = "material_cost", nullable = false, precision = 12, scale = 2)
    var materialCost: BigDecimal,
    @Column(name = "electricity_cost", nullable = false, precision = 12, scale = 2)
    var electricityCost: BigDecimal,
    @Column(name = "printer_wear_cost", nullable = false, precision = 12, scale = 2)
    var printerWearCost: BigDecimal,
    @Column(name = "labor_cost", nullable = false, precision = 12, scale = 2)
    var laborCost: BigDecimal,
    @Column(name = "margin_percentage", nullable = false, precision = 5, scale = 2)
    var marginPercentage: BigDecimal,
    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    var totalCost: BigDecimal,
    @Column(name = "suggested_price", nullable = false, precision = 12, scale = 2)
    var suggestedPrice: BigDecimal,
    @Column(nullable = false, length = 3)
    var currency: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: QuoteStatus = QuoteStatus.PENDING,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
)
