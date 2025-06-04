package one.effx.api.auth.controller

import jakarta.validation.Valid
import one.effx.api.auth.dto.*
import one.effx.api.auth.service.AuthService
import one.effx.domain.member.Member
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/v1/auth/register")
    suspend fun register(
        @Valid @RequestBody request: AuthRegisterRequest,
    ): ResponseEntity<Member> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/v1/auth/login")
    suspend fun login(
        @Valid
        @RequestBody
        request: AuthLoginRequest,
    ): ResponseEntity<AuthLoginResponse> {
        return ResponseEntity.ok(authService.login(request))
    }

    @PostMapping("/v1/auth/refresh")
    suspend fun refresh(
        @Valid
        @RequestBody
        request: AuthRefreshRequest,
    ): ResponseEntity<AuthRefreshResponse> {
        return ResponseEntity.ok(authService.refresh(request))
    }
}