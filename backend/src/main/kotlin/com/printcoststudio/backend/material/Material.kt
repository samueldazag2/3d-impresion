package com.printcoststudio.backend.material

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "materials")
class Material(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var type: String,
    @Column(nullable = false)
    var color: String,
    @Column(name = "price_per_kg", nullable = false, precision = 12, scale = 2)
    var pricePerKg: BigDecimal,
    @Column(name = "current_stock_grams", nullable = false, precision = 12, scale = 2)
    var currentStockGrams: BigDecimal,
    @Column(name = "density_grams_per_cm3", precision = 6, scale = 3)
    var densityGramsPerCm3: BigDecimal? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
)
