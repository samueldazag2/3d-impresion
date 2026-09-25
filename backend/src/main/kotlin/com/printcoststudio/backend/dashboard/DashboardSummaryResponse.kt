package com.printcoststudio.backend.dashboard

import com.printcoststudio.backend.material.MaterialResponse
import com.printcoststudio.backend.quote.QuoteStatus
import java.math.BigDecimal

data class DashboardSummaryResponse(
    val currency: String,
    val quotesByStatus: Map<QuoteStatus, Long>,
    /** Suggested price of quotes still in progress (pending, approved or printing). */
    val pipelineValue: BigDecimal,
    val deliveredRevenue: BigDecimal,
    val deliveredCost: BigDecimal,
    val deliveredProfit: BigDecimal,
    val lowStockThresholdGrams: BigDecimal,
    val lowStockMaterials: List<MaterialResponse>,
)
