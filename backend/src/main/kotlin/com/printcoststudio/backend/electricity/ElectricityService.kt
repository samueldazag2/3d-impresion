package com.printcoststudio.backend.electricity

import com.printcoststudio.backend.audit.AuditService
import com.printcoststudio.backend.common.CurrentUserProvider
import com.printcoststudio.backend.common.PageResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.util.UUID

@Service
class ElectricityService(
    private val electricityConsumptionRecordRepository: ElectricityConsumptionRecordRepository,
    private val auditService: AuditService,
    private val currentUserProvider: CurrentUserProvider,
) {
    fun list(pageable: Pageable): PageResponse<ElectricityRecordResponse> =
        PageResponse.of(
            electricityConsumptionRecordRepository.findAllByOrderByPeriodStartDesc(pageable),
            ElectricityRecordResponse::from,
        )

    @Transactional
    fun create(request: ElectricityRecordRequest): ElectricityRecordResponse {
        val record =
            electricityConsumptionRecordRepository.save(
                ElectricityConsumptionRecord(
                    periodStart = request.periodStart,
                    periodEnd = request.periodEnd,
                    kwhConsumed = request.kwhConsumed,
                    totalBillAmount = request.totalBillAmount,
                    notes = request.notes,
                ),
            )
        auditService.record(
            currentUserProvider.currentUserId(),
            "CREATE_ELECTRICITY_RECORD",
            "ElectricityConsumptionRecord",
            record.id,
        )
        return ElectricityRecordResponse.from(record)
    }

    @Transactional
    fun delete(id: UUID) {
        if (!electricityConsumptionRecordRepository.existsById(id)) {
            throw NoSuchElementException("Electricity record $id not found")
        }
        electricityConsumptionRecordRepository.deleteById(id)
        auditService.record(
            currentUserProvider.currentUserId(),
            "DELETE_ELECTRICITY_RECORD",
            "ElectricityConsumptionRecord",
            id,
        )
    }
}
