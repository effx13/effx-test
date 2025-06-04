package one.effx.application.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
@Profile(value = ["db"])
@ComponentScan(basePackages = ["one.effx.application"])
class ApplicationServiceConfig : AsyncConfigurer {
    @PostConstruct
    fun init() {
        log.info { "ApplicationServiceConfig Init" }
    }

    override fun getAsyncExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 50
            maxPoolSize = 50
            queueCapacity = 500
            setThreadNamePrefix("EVENT-")
            initialize()
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}