package one.effx.persistence.member.entity

import one.effx.domain.member.Member
import one.effx.domain.member.vo.MemberRole
import one.effx.persistence.abstracts.VersionEntity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("member")
class MemberEntity(
    @Id
    @Column("id")
    val id: Long,
    @Column("name")
    var name: String,
    @Column("email")
    var email: String,
    @Column("password_hash")
    var passwordHash: String,
    @Column("role")
    var role: MemberRole,
    @Column("is_active")
    var isActive: Boolean = true,
) : VersionEntity() {
    override fun getId(): Long = id
    override fun isNew() = id == 0L

    fun update(
        member: Member,
    ): MemberEntity {
        this.name = member.name
        this.email = member.email
        this.passwordHash = member.passwordHash
        this.role = member.role
        this.isActive = member.isActive
        return this
    }

    companion object {
        fun toDomain(
            it: MemberEntity,
        ): Member = Member(
            id = it.id,
            name = it.name,
            email = it.email,
            passwordHash = it.passwordHash,
            role = it.role,
            isActive = it.isActive,
        )

        fun toEntity(
            it: Member,
        ): MemberEntity = MemberEntity(
            id = it.id,
            name = it.name,
            email = it.email,
            passwordHash = it.passwordHash,
            role = it.role,
            isActive = it.isActive,
        )
    }
}