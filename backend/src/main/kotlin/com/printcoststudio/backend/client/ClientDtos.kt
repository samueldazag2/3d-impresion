package com.printcoststudio.backend.client

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.util.UUID

data class ClientRequest(
    @field:NotBlank
    val name: String,
    @field:Email
    val email: String? = null,
    val phone: String? = null,
    val notes: String? = null,
)

data class ClientResponse(
    val id: UUID,
    val name: String,
    val email: String?,
    val phone: String?,
    val notes: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun from(client: Client) =
            ClientResponse(
                id = client.id!!,
                name = client.name,
                email = client.email,
                phone = client.phone,
                notes = client.notes,
                createdAt = client.createdAt,
                updatedAt = client.updatedAt,
            )
    }
}
