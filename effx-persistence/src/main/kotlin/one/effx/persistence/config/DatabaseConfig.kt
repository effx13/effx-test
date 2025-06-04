package one.effx.persistence.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories(basePackages = ["one.effx.persistence"])
@EnableR2dbcAuditing
@ComponentScan(basePackages = ["one.effx.persistence"])
class DatabaseConfig {
}