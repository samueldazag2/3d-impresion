package com.printcoststudio.backend.dashboard

import com.printcoststudio.backend.material.MaterialRepository
import com.printcoststudio.backend.material.MaterialResponse
import com.printcoststudio.backend.quote.QuoteRepository
import com.printcoststudio.backend.quote.QuoteStatus
import com.printcoststudio.backend.settings.SettingsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class DashboardService(
    private val quoteRepository: QuoteRepository,
    private val materialRepository: MaterialRepository,
    private val settingsService: SettingsService,
) {
    @Transactional
    fun summary(): DashboardSummaryResponse {
        val totals = quoteRepository.totalsByStatus().associateBy { it.status }
        val delivered = totals[QuoteStatus.DELIVERED]
        val revenue = delivered?.suggestedPriceSum ?: BigDecimal.ZERO
        val cost = delivered?.totalCostSum ?: BigDecimal.ZERO
        val pipeline =
            totals
                .filterKeys { it != QuoteStatus.DELIVERED }
                .values
                .fold(BigDecimal.ZERO) { acc, t -> acc + t.suggestedPriceSum }

        return DashboardSummaryResponse(
            currency = settingsService.getOrCreateSingleton().currency,
            quotesByStatus = QuoteStatus.entries.associateWith { totals[it]?.count ?: 0L },
            pipelineValue = pipeline,
            deliveredRevenue = revenue,
            deliveredCost = cost,
            deliveredProfit = revenue - cost,
            lowStockThresholdGrams = LOW_STOCK_THRESHOLD_GRAMS,
            lowStockMaterials =
                materialRepository
                    .findByCurrentStockGramsLessThanOrderByCurrentStockGramsAsc(LOW_STOCK_THRESHOLD_GRAMS)
                    .map(MaterialResponse::from),
        )
    }

    companion object {
        /** A spool under this weight is nearly empty. */
        val LOW_STOCK_THRESHOLD_GRAMS: BigDecimal = BigDecimal("250")
    }
}
