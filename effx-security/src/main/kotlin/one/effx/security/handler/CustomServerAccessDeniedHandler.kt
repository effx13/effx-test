package one.effx.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CustomServerAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : ServerAccessDeniedHandler {
    override fun handle(
        exchange: ServerWebExchange,
        denied: AccessDeniedException
    ): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.FORBIDDEN
        response.headers.contentType = MediaType.APPLICATION_JSON

        val errorResponse = mapOf(
            "error" to "FORBIDDEN",
            "message" to "접근이 거부된 요청입니다."
        )

        return response.writeWith(
            Mono.just(
                response.bufferFactory().wrap(
                    objectMapper.writeValueAsBytes(errorResponse)
                )
            )
        )
    }
}