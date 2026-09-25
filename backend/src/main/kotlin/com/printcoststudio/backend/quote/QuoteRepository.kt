package com.printcoststudio.backend.quote

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

    @Query(
        "select new com.printcoststudio.backend.quote.QuoteStatusTotals(" +
            "q.status, count(q), sum(q.suggestedPrice), sum(q.totalCost)) " +
            "from Quote q group by q.status",
    )
    fun totalsByStatus(): List<QuoteStatusTotals>
}
