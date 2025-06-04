package one.effx.domain.member.command

import one.effx.domain.member.Member
import one.effx.domain.member.repository.MemberRepository
import one.effx.domain.member.vo.MemberRole

class SaveMemberCommand(
    private val memberRepository: MemberRepository
) {
    suspend fun handle(
        name: String,
        email: String,
        passwordHash: String,
        role: MemberRole,
    ): Member {
        val member = Member.create(
            id = 0,
            name = name,
            email = email,
            passwordHash = passwordHash,
            role = role,
        )

        return memberRepository.save(member)
    }
}