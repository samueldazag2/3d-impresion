package com.printcoststudio.backend.settings

import com.printcoststudio.backend.audit.AuditService
import com.printcoststudio.backend.common.CurrentUserProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class SettingsService(
    private val settingsRepository: SettingsRepository,
    private val auditService: AuditService,
    private val currentUserProvider: CurrentUserProvider,
) {
    fun get(): SettingsResponse = SettingsResponse.from(getOrCreateSingleton())

    @Transactional
    fun update(request: SettingsRequest): SettingsResponse {
        val settings = getOrCreateSingleton()
        settings.currency = request.currency
        settings.electricityRatePerKwh = request.electricityRatePerKwh
        settings.printerPowerConsumptionWatts = request.printerPowerConsumptionWatts
        settings.printerPurchasePrice = request.printerPurchasePrice
        settings.printerLifespanHours = request.printerLifespanHours
        settings.defaultMarginPercentage = request.defaultMarginPercentage
        settings.laborHourlyRate = request.laborHourlyRate
        settings.updatedAt = Instant.now()

        auditService.record(currentUserProvider.currentUserId(), "UPDATE_SETTINGS", "Settings", settings.id)
        return SettingsResponse.from(settings)
    }

    /** Exposed for QuoteService, which needs the live entity rather than the response DTO. */
    @Transactional
    fun getOrCreateSingleton(): Settings =
        settingsRepository.findById(Settings.SINGLETON_ID).orElseGet {
            settingsRepository.save(Settings.default())
        }
}
