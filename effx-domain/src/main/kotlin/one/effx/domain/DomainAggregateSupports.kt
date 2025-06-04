package one.effx.domain

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

interface DomainEvent {
    fun eventSource(): List<Any> = emptyList() // 이벤트를 저장을 해야 하는 경우에 사용

    fun publisher(): Boolean = true // 이벤트를 발행할지 말지 결정

    fun order(): Int = 0 // 이벤트의 순서를 결정
}

interface DomainCommand<C> {
    fun handle(): C
}