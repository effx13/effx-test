package one.effx.api.auth.dto

import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated

@Validated
data class AuthLoginRequest(
    @field:NotBlank
    val email: String,
    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String,
)