package one.effx.domain

import org.springframework.context.ApplicationEventPublisher

abstract class AggregateRoot<T : DomainEvent, ID> {
    private val _eventsPendingPersistence: MutableList<T> = mutableListOf()

    internal fun registerEvent(event: T) {
        _eventsPendingPersistence.add(event)
    }

    abstract val id: ID

    fun domainEvents(): List<T> {
        return _eventsPendingPersistence.toList()
    }

    fun clearDomainEvents() {
        _eventsPendingPersistence.clear()
    }
}

interface AggregateRepository<T : AggregateRoot<*, ID>, ID> {
    suspend fun save(aggregate: T): T
    suspend fun findById(id: ID): T
}

abstract class EventAggregateRepository<T : AggregateRoot<E, ID>, ID, E : DomainEvent>(
    private val publisher: ApplicationEventPublisher
) : AggregateRepository<T, ID> {

    override suspend fun save(aggregate: T): T {
        val saved = doSave(aggregate)
        publishEventsFrom(saved, publisher)
        return saved
    }

    abstract suspend fun doSave(aggregate: T): T

    override suspend fun findById(id: ID): T {
        throw NotImplementedError("Must be implemented")
    }
}

interface DomainEvent {
    fun eventSource(): List<Any> = emptyList() // 이벤트를 저장을 해야 하는 경우에 사용

    fun publisher(): Boolean = true // 이벤트를 발행할지 말지 결정

    fun order(): Int = 0 // 이벤트의 순서를 결정
}

interface DomainCommand<C> {
    fun handle(): C
}

fun <T : DomainEvent> publishEventsFrom(
    aggregate: AggregateRoot<T, *>,
    publisher: ApplicationEventPublisher,
) {
    aggregate.domainEvents()
        .sortedBy { it.order() }
        .filter { it.publisher() }
        .forEach { event ->
            publisher.publishEvent(event)
        }

    aggregate.clearDomainEvents()
}