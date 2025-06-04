package one.effx.domain.member.repository

import one.effx.domain.AggregateRepository
import one.effx.domain.member.Member

interface MemberRepository: AggregateRepository<Member, Long> {
    suspend fun findByEmail(email: String): Member?
    suspend fun existsByEmail(email: String): Boolean
}