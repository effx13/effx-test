package one.effx.application.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import one.effx.application.interceptor.DomainEventInterceptor
import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.*
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

    override fun getAsyncExecutor(): Executor = ThreadPoolTaskExecutor().apply {
        corePoolSize = 50
        maxPoolSize = 50
        queueCapacity = 1000
        threadNamePrefix = "EVENT-"
        initialize()
    }

    @Bean
    fun domainEventAdvisor(
        applicationEventPublisher: ApplicationEventPublisher,
        @Value("\${domain.event.disable:false}") disable: Boolean,
    ): Advisor {
        val expressionPointcut = AspectJExpressionPointcut()
        expressionPointcut.expression = "execution(* one.effx.domain.*Repository.save(*))"

        val pointcutAdvisor = DefaultPointcutAdvisor(
            expressionPointcut,
            DomainEventInterceptor(
                applicationEventPublisher = applicationEventPublisher,
                disable = disable,
            )
        )
        pointcutAdvisor.order = Int.MAX_VALUE

        return pointcutAdvisor
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}