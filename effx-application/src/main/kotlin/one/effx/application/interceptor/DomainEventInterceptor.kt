package one.effx.application.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import one.effx.domain.AggregateRoot
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.context.ApplicationEventPublisher

class DomainEventInterceptor(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val disable: Boolean = false,
) : MethodInterceptor {
    override fun invoke(invocation: MethodInvocation): Any? {
        try {
            val aggregate = invocation.arguments[0]!!
            val res = invocation.proceed()
            domainEventPublisher(aggregate)
            return res
        } catch (e: Throwable) {
            logger.error(e) { "Aggregate Repository Save Failed: ${e.message}" }
            throw e
        }
    }

    private fun domainEventPublisher(
        aggregate: Any
    ) {
        if (aggregate is AggregateRoot<*, *>) {
            aggregate.domainEvents().sortedBy { it.order() }
                .forEach {
                    if (it.publisher()) {
                        logger.debug { "Publish Domain Event: $it" }
                        if (disable.not()) {
                            applicationEventPublisher.publishEvent(it)
                        }
                    }
                }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}