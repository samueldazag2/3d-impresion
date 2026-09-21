package com.printcoststudio.backend.electricity

import com.printcoststudio.backend.common.PageResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/electricity-records")
class ElectricityController(
    private val electricityService: ElectricityService,
) {
    @GetMapping
    fun list(pageable: Pageable): PageResponse<ElectricityRecordResponse> = electricityService.list(pageable)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: ElectricityRecordRequest,
    ): ElectricityRecordResponse = electricityService.create(request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: UUID,
    ) = electricityService.delete(id)
}
