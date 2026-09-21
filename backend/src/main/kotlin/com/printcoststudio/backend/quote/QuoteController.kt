package com.printcoststudio.backend.quote

import com.printcoststudio.backend.common.PageResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/quotes")
class QuoteController(
    private val quoteService: QuoteService,
) {
    @GetMapping
    fun list(
        @RequestParam(required = false) clientId: UUID?,
        @RequestParam(required = false) status: QuoteStatus?,
        pageable: Pageable,
    ): PageResponse<QuoteResponse> = quoteService.list(clientId, status, pageable)

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: UUID,
    ): QuoteResponse = quoteService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: QuoteRequest,
    ): QuoteResponse = quoteService.create(request)

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: QuoteStatusUpdateRequest,
    ): QuoteResponse = quoteService.updateStatus(id, request.status)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: UUID,
    ) = quoteService.delete(id)
}
