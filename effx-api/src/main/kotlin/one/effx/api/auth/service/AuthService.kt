package one.effx.api.auth.service

import kotlinx.coroutines.reactive.awaitFirstOrNull
import one.effx.api.auth.dto.AuthLoginRequest
import one.effx.api.auth.dto.AuthLoginResponse
import one.effx.api.auth.dto.AuthRefreshRequest
import one.effx.api.auth.dto.AuthRefreshResponse
import one.effx.api.auth.dto.AuthRegisterRequest
import one.effx.application.member.usecase.MemberUseCase
import one.effx.domain.member.Member
import one.effx.domain.member.vo.MemberRole
import one.effx.security.MemberPrincipal
import one.effx.security.exception.AuthException
import one.effx.security.provider.JwtTokenProvider
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberUseCase: MemberUseCase,
    private val reactiveAuthenticationManager: ReactiveAuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    suspend fun register(authRegisterRequest: AuthRegisterRequest): Member {
        return memberUseCase.saveMember(
            name = authRegisterRequest.name,
            email = authRegisterRequest.email,
            passwordHash = passwordEncoder.encode(authRegisterRequest.password),
            role = MemberRole.ROLE_USER
        )
    }

    suspend fun login(request: AuthLoginRequest): AuthLoginResponse {
        val authentication = reactiveAuthenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        ).awaitFirstOrNull() ?: throw AuthException.memberNotMatch()

        if (!authentication.isAuthenticated || authentication.principal !is MemberPrincipal) {
            throw AuthException.invalidToken()
        }

        val refreshToken = jwtTokenProvider.generateRefreshToken(authentication)
        val accessToken = jwtTokenProvider.generateAccessToken(authentication)

        return AuthLoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    suspend fun refresh(request: AuthRefreshRequest): AuthRefreshResponse {
        val accessToken = jwtTokenProvider.refreshAccessToken(request.refreshToken)

        return AuthRefreshResponse(
            accessToken = accessToken,
        )
    }
}