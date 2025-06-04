package one.effx.security.filter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.withContext
import one.effx.security.provider.JwtTokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.CoWebFilter
import org.springframework.web.server.CoWebFilterChain
import org.springframework.web.server.ServerWebExchange

class JwtTokenAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : CoWebFilter() {
    override suspend fun filter(exchange: ServerWebExchange, chain: CoWebFilterChain) {
        val token = resolveAccessToken(exchange.request)

        if (!token.isNullOrBlank()) {
            val authentication = withContext(Dispatchers.IO) {
                jwtTokenProvider.getAuthentication(token)
            }

            val securityContext = ReactiveSecurityContextHolder.withAuthentication(authentication)

            withContext(ReactorContext(securityContext)) {
                chain.filter(exchange)
            }
        } else {
            chain.filter(exchange)
        }
    }

    private fun resolveAccessToken(request: ServerHttpRequest): String? {
        val authorizationHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        return if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader.substring(7)
        } else {
            null
        }
    }
}