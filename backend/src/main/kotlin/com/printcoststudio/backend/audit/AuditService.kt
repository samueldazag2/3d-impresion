package com.printcoststudio.backend.audit

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuditService(
    private val auditLogRepository: AuditLogRepository,
) {
    fun record(
        userId: UUID,
        action: String,
        entityType: String? = null,
        entityId: UUID? = null,
    ) {
        auditLogRepository.save(
            AuditLog(userId = userId, action = action, entityType = entityType, entityId = entityId),
        )
    }
}
