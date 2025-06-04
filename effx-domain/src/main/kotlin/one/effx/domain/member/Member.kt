package one.effx.domain.member

import com.fasterxml.jackson.annotation.JsonIgnore
import one.effx.domain.AggregateRoot
import one.effx.domain.member.event.MemberCreatedEvent
import one.effx.domain.member.event.MemberEvent
import one.effx.domain.member.vo.MemberRole
import java.time.LocalDateTime

data class Member(
    @JsonIgnore
    override val id: Long,
    val name: String,
    val email: String,
    @JsonIgnore
    val passwordHash: String,
    val role: MemberRole = MemberRole.ROLE_USER,
    @JsonIgnore
    val isActive: Boolean = true,
    @JsonIgnore
    val deletedAt: LocalDateTime? = null
): AggregateRoot<MemberEvent, Long>() {

    companion object {
        fun create(
            id: Long,
            name: String,
            email: String,
            passwordHash: String,
            role: MemberRole = MemberRole.ROLE_USER,
        ): Member = Member(
            id = id,
            name = name,
            email = email,
            passwordHash = passwordHash,
            role = role,
        ).apply {
            registerEvent(MemberCreatedEvent(id, name, email, role))
        }
    }
}