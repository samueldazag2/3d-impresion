package com.printcoststudio.backend.electricity

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ElectricityConsumptionRecordRepository : JpaRepository<ElectricityConsumptionRecord, UUID> {
    fun findAllByOrderByPeriodStartDesc(pageable: Pageable): Page<ElectricityConsumptionRecord>
}
