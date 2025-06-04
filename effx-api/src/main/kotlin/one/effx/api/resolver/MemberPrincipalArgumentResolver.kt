package one.effx.api.resolver

import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import one.effx.api.annotation.MemberPrincipal
import one.effx.domain.member.repository.MemberRepository
import one.effx.security.exception.AuthException
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class MemberPrincipalArgumentResolver(
    private val memberRepository: MemberRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.hasParameterAnnotation(MemberPrincipal::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> = mono {
        val securityContext = ReactiveSecurityContextHolder.getContext().awaitSingleOrNull()
            ?: throw AuthException.notLoggedIn()

        val authentication = securityContext.authentication
            ?: throw AuthException.notLoggedIn()

        val principal = authentication.principal as? User

        val email = principal?.username
            ?: throw AuthException.notLoggedIn()

        val member = memberRepository.findByEmail(email)
            ?: throw AuthException.notLoggedIn()

        member
    }
}
