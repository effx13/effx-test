package one.effx.persistence.member.adapter

import one.effx.domain.EventAggregateRepository
import one.effx.domain.member.Member
import one.effx.domain.member.event.MemberEvent
import one.effx.domain.member.repository.MemberRepository
import one.effx.persistence.member.entity.MemberEntity
import one.effx.persistence.member.repository.MemberCrudRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Repository

@Repository
class MemberAdapter(
    publisher: ApplicationEventPublisher,
    private val memberCrudRepository: MemberCrudRepository,
) : EventAggregateRepository<Member, Long, MemberEvent>(publisher), MemberRepository {
    override suspend fun findByEmail(email: String) = memberCrudRepository
        .findByEmail(email)
        ?.let { MemberEntity.toDomain(it) }

    override suspend fun doSave(aggregate: Member): Member =
        aggregate.apply {
            val saved = memberCrudRepository.findById(aggregate.id)?.let { entity ->
                entity.update(aggregate)
                val savedEntity = memberCrudRepository.save(entity)
                MemberEntity.toDomain(savedEntity)
            } ?: run {
                val entity = MemberEntity.toEntity(aggregate)
                val savedEntity = memberCrudRepository.save(entity)
                MemberEntity.toDomain(savedEntity)
            }

            this.id = saved.id // 요청된 Aggregate의 ID를 저장된 Aggregate의 ID로 업데이트(이벤트 보존을 위해)
        }


    override suspend fun findById(id: Long): Member =
        memberCrudRepository.findById(id)?.let { MemberEntity.toDomain(it) }
            ?: throw IllegalArgumentException("Member with id $id not found")


    override suspend fun existsByEmail(email: String): Boolean {
        return memberCrudRepository.existsByEmail(email)
    }
}