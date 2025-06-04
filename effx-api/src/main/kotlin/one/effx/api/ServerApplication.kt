package one.effx.api

import one.effx.application.config.ApplicationServiceConfig
import one.effx.persistence.config.DatabaseConfig
import one.effx.security.config.SecurityConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication(
    scanBasePackages = ["one.effx.api"]
)
@Import(
    DatabaseConfig::class,
    ApplicationServiceConfig::class,
    SecurityConfig::class,
)
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}