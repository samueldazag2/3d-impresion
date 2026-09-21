package com.printcoststudio.backend.client

import com.printcoststudio.backend.common.PageResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/clients")
class ClientController(
    private val clientService: ClientService,
) {
    @GetMapping
    fun list(
        @RequestParam(required = false) search: String?,
        pageable: Pageable,
    ): PageResponse<ClientResponse> = clientService.list(search, pageable)

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: UUID,
    ): ClientResponse = clientService.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: ClientRequest,
    ): ClientResponse = clientService.create(request)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: ClientRequest,
    ): ClientResponse = clientService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: UUID,
    ) = clientService.delete(id)
}
