package one.effx.security.service

import kotlinx.coroutines.reactor.mono
import one.effx.domain.member.repository.MemberRepository
import one.effx.security.MemberPrincipal
import one.effx.security.exception.AuthException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository,
): ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> = mono {
        username ?: throw AuthException.memberNotMatch()
        memberRepository.findByEmail(username)
            ?.let { MemberPrincipal(it) }
            ?: throw AuthException.memberNotMatch()
    }
}