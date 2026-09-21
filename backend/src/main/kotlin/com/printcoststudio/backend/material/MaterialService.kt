package com.printcoststudio.backend.material

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
class MaterialService(
    private val materialRepository: MaterialRepository,
    private val auditService: AuditService,
    private val currentUserProvider: CurrentUserProvider,
) {
    fun list(
        search: String?,
        pageable: Pageable,
    ): PageResponse<MaterialResponse> {
        val page =
            if (search.isNullOrBlank()) {
                materialRepository.findAll(pageable)
            } else {
                materialRepository.findByNameContainingIgnoreCase(search, pageable)
            }
        return PageResponse.of(page, MaterialResponse::from)
    }

    fun get(id: UUID): MaterialResponse =
        MaterialResponse.from(
            materialRepository.findById(id).orElseThrow { NoSuchElementException("Material $id not found") },
        )

    @Transactional
    fun create(request: MaterialRequest): MaterialResponse {
        val material =
            materialRepository.save(
                Material(
                    name = request.name,
                    type = request.type,
                    color = request.color,
                    pricePerKg = request.pricePerKg,
                    currentStockGrams = request.currentStockGrams,
                    densityGramsPerCm3 = request.densityGramsPerCm3,
                ),
            )
        auditService.record(currentUserProvider.currentUserId(), "CREATE_MATERIAL", "Material", material.id)
        return MaterialResponse.from(material)
    }

    @Transactional
    fun update(
        id: UUID,
        request: MaterialRequest,
    ): MaterialResponse {
        val material =
            materialRepository.findById(id).orElseThrow { NoSuchElementException("Material $id not found") }
        material.name = request.name
        material.type = request.type
        material.color = request.color
        material.pricePerKg = request.pricePerKg
        material.currentStockGrams = request.currentStockGrams
        material.densityGramsPerCm3 = request.densityGramsPerCm3
        material.updatedAt = Instant.now()

        auditService.record(currentUserProvider.currentUserId(), "UPDATE_MATERIAL", "Material", material.id)
        return MaterialResponse.from(material)
    }

    @Transactional
    fun delete(id: UUID) {
        if (!materialRepository.existsById(id)) {
            throw NoSuchElementException("Material $id not found")
        }
        materialRepository.deleteById(id)
        auditService.record(currentUserProvider.currentUserId(), "DELETE_MATERIAL", "Material", id)
    }
}
