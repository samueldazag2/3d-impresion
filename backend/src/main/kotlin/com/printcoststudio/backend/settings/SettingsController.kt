package com.printcoststudio.backend.settings

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/settings")
class SettingsController(
    private val settingsService: SettingsService,
) {
    @GetMapping
    fun get(): SettingsResponse = settingsService.get()

    @PutMapping
    fun update(
        @Valid @RequestBody request: SettingsRequest,
    ): SettingsResponse = settingsService.update(request)
}
