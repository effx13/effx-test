package one.effx.domain.member.event

import one.effx.domain.DomainEvent
import one.effx.domain.member.vo.MemberRole

sealed class MemberEvent : DomainEvent

data class MemberCreatedEvent(
    val id: Long,
    val name: String,
    val email: String,
    val role: MemberRole,
) : MemberEvent() {
    override fun order() = Int.MIN_VALUE + 1
}