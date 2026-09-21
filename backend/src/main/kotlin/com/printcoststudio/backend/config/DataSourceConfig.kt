package com.printcoststudio.backend.config

import org.flywaydb.core.Flyway
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * Spring Boot 4 dropped its built-in Flyway autoconfiguration, so migrations
 * are run here, synchronously, while building the DataSource bean. Because
 * JPA's EntityManagerFactory depends on this same DataSource bean, Spring's
 * normal bean-creation order guarantees the schema exists before Hibernate
 * validates it - no extra dependsOn wiring needed.
 */
@Configuration
class DataSourceConfig {
    @Bean
    fun dataSource(properties: DataSourceProperties): DataSource {
        val dataSource = properties.initializeDataSourceBuilder().build()
        Flyway
            .configure()
            .dataSource(dataSource)
            .load()
            .migrate()
        return dataSource
    }
}
