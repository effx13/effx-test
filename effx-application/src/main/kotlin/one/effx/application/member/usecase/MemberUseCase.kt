package one.effx.application.member.usecase

import one.effx.domain.member.Member
import one.effx.domain.member.command.SaveMemberCommand
import one.effx.domain.member.exception.MemberException
import one.effx.domain.member.repository.MemberRepository
import one.effx.domain.member.vo.MemberRole
import org.springframework.stereotype.Component

@Component
class MemberUseCase(
    private val memberRepository: MemberRepository
) {
    private val saveMemberCommand = SaveMemberCommand(memberRepository)

    suspend fun saveMember(
        name: String,
        email: String,
        passwordHash: String,
        role: MemberRole
    ): Member {
        if (memberRepository.existsByEmail(email)) {
            throw MemberException.emailAlreadyExists()
        }

        return saveMemberCommand.handle(
            name = name,
            email = email,
            passwordHash = passwordHash,
            role = role
        )
    }
}