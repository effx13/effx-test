package one.effx.api.auth.dto

import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated

@Validated
data class AuthRefreshRequest(
    @field:NotBlank(message = "리프레시 토큰은 필수입니다.")
    val refreshToken: String,
)