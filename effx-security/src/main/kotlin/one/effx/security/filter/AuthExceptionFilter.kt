package one.effx.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import one.effx.common.exception.EffxException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class AuthExceptionFilter(
    private val objectMapper: ObjectMapper,
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        return chain.filter(exchange)
            .onErrorResume(
                Exception::class.java
            ) { e ->
                if (e !is JwtException && e !is EffxException) {
                    return@onErrorResume Mono.error(e)
                }

                val response = exchange.response
                response.statusCode = HttpStatus.UNAUTHORIZED
                response.headers.contentType = MediaType.APPLICATION_JSON

                val errorMap = mutableMapOf<String, String>()

                when (e) {
                    is ExpiredJwtException -> {
                        errorMap["error"] = "TOKEN_EXPIRED"
                        errorMap["message"] = "만료된 토큰입니다."
                    }
                    is MalformedJwtException -> {
                        errorMap["error"] = "TOKEN_MALFORMED"
                        errorMap["message"] = "잘못된 형식의 토큰입니다."
                    }
                    is UnsupportedJwtException -> {
                        errorMap["error"] = "TOKEN_UNSUPPORTED"
                        errorMap["message"] = "지원하지 않는 토큰입니다."
                    }
                    is SignatureException -> {
                        errorMap["error"] = "TOKEN_SIGNATURE_INVALID"
                        errorMap["message"] = "유효하지 않은 서명의 토큰입니다."
                    }
                    is EffxException -> {
                        errorMap["error"] = e.errorCode()
                        errorMap["message"] = e.message ?: e.toString()
                    }
                    else -> {
                        errorMap["error"] = "UNAUTHORIZED"
                        errorMap["message"] = "인증되지 않은 요청입니다."
                    }
                }

                try {
                    return@onErrorResume exchange.response.writeWith(
                        objectMapper.writeValueAsBytes(errorMap)
                            .let { Mono.just(exchange.response.bufferFactory().wrap(it)) }
                    )
                } catch (ex: Exception) {
                    return@onErrorResume Mono.error(ex)
                }
            }
    }
}