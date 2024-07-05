package com.roronoadiogo.playground.webflux.infrastructure.config

import io.r2dbc.spi.ConnectionFactory
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.r2dbc.connection.ConnectionFactoryUtils
import org.springframework.r2dbc.core.DatabaseClient

@Configuration
class DatabaseConfig(@Value("\${spring.flyway.url}") private val url: String,
                     @Value("\${spring.flyway.user}") private val user: String,
                     @Value("\${spring.flyway.password}") private val password: String) {

    @Bean(initMethod = "migrate")
    fun flywayConfigurationReactive(): Flyway {
        return Flyway(Flyway.configure().dataSource(url, user, password))
    }

    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient {
        return DatabaseClient.create(connectionFactory)
    }
}