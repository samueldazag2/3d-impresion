package com.printcoststudio.backend.settings

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SettingsRepository : JpaRepository<Settings, UUID>
