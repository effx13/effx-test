package one.effx.persistence.member.repository

import one.effx.persistence.member.entity.MemberEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MemberCrudRepository: CoroutineCrudRepository<MemberEntity, Long> {
    suspend fun findByEmail(email: String): MemberEntity?
    suspend fun existsByEmail(email: String): Boolean
}