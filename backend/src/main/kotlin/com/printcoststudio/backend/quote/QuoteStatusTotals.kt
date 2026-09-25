package com.printcoststudio.backend.quote

import java.math.BigDecimal

data class QuoteStatusTotals(
    val status: QuoteStatus,
    val count: Long,
    val suggestedPriceSum: BigDecimal,
    val totalCostSum: BigDecimal,
)
