package com.printcoststudio.backend.quote

import com.printcoststudio.backend.audit.AuditService
import com.printcoststudio.backend.client.ClientRepository
import com.printcoststudio.backend.common.CurrentUserProvider
import com.printcoststudio.backend.common.PageResponse
import com.printcoststudio.backend.material.MaterialRepository
import com.printcoststudio.backend.settings.SettingsService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.NoSuchElementException
import java.util.UUID

@Service
class QuoteService(
    private val quoteRepository: QuoteRepository,
    private val materialRepository: MaterialRepository,
    private val clientRepository: ClientRepository,
    private val settingsService: SettingsService,
    private val auditService: AuditService,
    private val currentUserProvider: CurrentUserProvider,
) {
    fun list(
        clientId: UUID?,
        status: QuoteStatus?,
        pageable: Pageable,
    ): PageResponse<QuoteResponse> {
        val page =
            when {
                clientId != null -> quoteRepository.findByClientId(clientId, pageable)
                status != null -> quoteRepository.findByStatus(status, pageable)
                else -> quoteRepository.findAll(pageable)
            }
        return PageResponse.of(page, QuoteResponse::from)
    }

    fun get(id: UUID): QuoteResponse =
        QuoteResponse.from(quoteRepository.findById(id).orElseThrow { NoSuchElementException("Quote $id not found") })

    @Transactional
    fun create(request: QuoteRequest): QuoteResponse {
        val material =
            materialRepository
                .findById(request.materialId)
                .orElseThrow { NoSuchElementException("Material ${request.materialId} not found") }
        val client =
            request.clientId?.let { clientId ->
                clientRepository
                    .findById(clientId)
                    .orElseThrow { NoSuchElementException("Client $clientId not found") }
            }
        val settings = settingsService.getOrCreateSingleton()
        val marginPercentage = request.marginPercentage ?: settings.defaultMarginPercentage

        val breakdown =
            QuoteCostCalculator.calculate(
                CostCalculationInput(
                    materialGramsUsed = request.materialGramsUsed,
                    materialPricePerKg = material.pricePerKg,
                    printTimeHours = request.printTimeHours,
                    printerPowerConsumptionWatts = settings.printerPowerConsumptionWatts,
                    electricityRatePerKwh = settings.electricityRatePerKwh,
                    printerPurchasePrice = settings.printerPurchasePrice,
                    printerLifespanHours = settings.printerLifespanHours,
                    laborHours = request.laborHours,
                    laborHourlyRate = settings.laborHourlyRate,
                    packagingCost = request.packagingCost,
                    marginPercentage = marginPercentage,
                ),
            )

        val quote =
            quoteRepository.save(
                Quote(
                    client = client,
                    material = material,
                    title = request.title,
                    materialGramsUsed = request.materialGramsUsed,
                    printTimeHours = request.printTimeHours,
                    laborHours = request.laborHours,
                    packagingCost = breakdown.packagingCost,
                    materialCost = breakdown.materialCost,
                    electricityCost = breakdown.electricityCost,
                    printerWearCost = breakdown.printerWearCost,
                    laborCost = breakdown.laborCost,
                    marginPercentage = marginPercentage,
                    totalCost = breakdown.totalCost,
                    suggestedPrice = breakdown.suggestedPrice,
                    currency = settings.currency,
                ),
            )

        auditService.record(currentUserProvider.currentUserId(), "CREATE_QUOTE", "Quote", quote.id)
        return QuoteResponse.from(quote)
    }

    @Transactional
    fun updateStatus(
        id: UUID,
        status: QuoteStatus,
    ): QuoteResponse {
        val quote = quoteRepository.findById(id).orElseThrow { NoSuchElementException("Quote $id not found") }
        quote.status = status
        quote.updatedAt = Instant.now()

        auditService.record(currentUserProvider.currentUserId(), "UPDATE_QUOTE_STATUS", "Quote", quote.id)
        return QuoteResponse.from(quote)
    }

    @Transactional
    fun delete(id: UUID) {
        if (!quoteRepository.existsById(id)) {
            throw NoSuchElementException("Quote $id not found")
        }
        quoteRepository.deleteById(id)
        auditService.record(currentUserProvider.currentUserId(), "DELETE_QUOTE", "Quote", id)
    }
}
