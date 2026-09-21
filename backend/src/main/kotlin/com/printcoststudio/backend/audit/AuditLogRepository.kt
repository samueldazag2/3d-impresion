package com.printcoststudio.backend.audit

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuditLogRepository : JpaRepository<AuditLog, UUID> {
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<AuditLog>
}
