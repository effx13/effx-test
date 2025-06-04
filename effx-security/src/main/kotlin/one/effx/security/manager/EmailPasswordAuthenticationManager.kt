package one.effx.security.manager

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import one.effx.security.MemberPrincipal
import one.effx.security.exception.AuthException
import org.springframework.security.authentication.AccountStatusUserDetailsChecker
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono

class EmailPasswordAuthenticationManager(
    private val reactiveUserDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : ReactiveAuthenticationManager {
    private val detailsChecker = AccountStatusUserDetailsChecker()

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return Mono.just(authentication)
            .filter { it is UsernamePasswordAuthenticationToken }
            .cast(UsernamePasswordAuthenticationToken::class.java)
            .flatMap {
                mono {
                    validate(it)
                }
            }
    }

    private suspend fun validate(authentication: Authentication): Authentication {
        val phoneNumber = authentication.name.toString()
        val rawPassword = authentication.credentials?.toString()
            ?: throw AuthException.memberNotMatch()

        val user = reactiveUserDetailsService.findByUsername(
            phoneNumber
        ).awaitFirstOrNull() ?: throw AuthException.invalidToken()

        detailsChecker.check(user)

        if (user !is MemberPrincipal) {
            throw AuthException.memberNotMatch()
        }

        if (!passwordEncoder.matches(rawPassword, user.password)) {
            throw AuthException.memberNotMatch()
        }

        return UsernamePasswordAuthenticationToken(
            user,
            null,
            user.authorities
        )
    }
}