package com.printcoststudio.backend.quote

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface QuoteRepository : JpaRepository<Quote, UUID> {
    fun findByClientId(
        clientId: UUID,
        pageable: Pageable,
    ): Page<Quote>

    fun findByStatus(
        status: QuoteStatus,
        pageable: Pageable,
    ): Page<Quote>
}
