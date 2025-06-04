package one.effx.persistence.member.adapter

import one.effx.domain.member.Member
import one.effx.domain.member.repository.MemberRepository
import one.effx.persistence.member.entity.MemberEntity
import one.effx.persistence.member.repository.MemberCrudRepository
import org.springframework.stereotype.Repository

@Repository
class MemberAdapter(
    private val memberCrudRepository: MemberCrudRepository
): MemberRepository {
    override suspend fun findByEmail(email: String) = memberCrudRepository
        .findByEmail(email)
        ?.let { MemberEntity.toDomain(it) }


    override suspend fun save(aggregate: Member): Member =
        memberCrudRepository.findById(aggregate.id)?.let { entity ->
            entity.update(aggregate)
            memberCrudRepository.save(entity)
            MemberEntity.toDomain(entity)
        } ?: run {
            val entity = MemberEntity.toEntity(aggregate)
            val savedEntity = memberCrudRepository.save(entity)
            MemberEntity.toDomain(savedEntity)
        }


    override suspend fun findById(id: Long): Member =
        memberCrudRepository.findById(id)?.let { MemberEntity.toDomain(it) }
            ?: throw IllegalArgumentException("Member with id $id not found")
}