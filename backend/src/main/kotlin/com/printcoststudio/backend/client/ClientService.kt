package com.printcoststudio.backend.client

import com.printcoststudio.backend.audit.AuditService
import com.printcoststudio.backend.common.CurrentUserProvider
import com.printcoststudio.backend.common.PageResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.NoSuchElementException
import java.util.UUID

@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val auditService: AuditService,
    private val currentUserProvider: CurrentUserProvider,
) {
    fun list(
        search: String?,
        pageable: Pageable,
    ): PageResponse<ClientResponse> {
        val page =
            if (search.isNullOrBlank()) {
                clientRepository.findAll(pageable)
            } else {
                clientRepository.findByNameContainingIgnoreCase(search, pageable)
            }
        return PageResponse.of(page, ClientResponse::from)
    }

    fun get(id: UUID): ClientResponse =
        ClientResponse.from(clientRepository.findById(id).orElseThrow { NoSuchElementException("Client $id not found") })

    @Transactional
    fun create(request: ClientRequest): ClientResponse {
        val client =
            clientRepository.save(
                Client(name = request.name, email = request.email, phone = request.phone, notes = request.notes),
            )
        auditService.record(currentUserProvider.currentUserId(), "CREATE_CLIENT", "Client", client.id)
        return ClientResponse.from(client)
    }

    @Transactional
    fun update(
        id: UUID,
        request: ClientRequest,
    ): ClientResponse {
        val client = clientRepository.findById(id).orElseThrow { NoSuchElementException("Client $id not found") }
        client.name = request.name
        client.email = request.email
        client.phone = request.phone
        client.notes = request.notes
        client.updatedAt = Instant.now()

        auditService.record(currentUserProvider.currentUserId(), "UPDATE_CLIENT", "Client", client.id)
        return ClientResponse.from(client)
    }

    @Transactional
    fun delete(id: UUID) {
        if (!clientRepository.existsById(id)) {
            throw NoSuchElementException("Client $id not found")
        }
        clientRepository.deleteById(id)
        auditService.record(currentUserProvider.currentUserId(), "DELETE_CLIENT", "Client", id)
    }
}
