package one.effx.api.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated

@Validated
data class AuthRegisterRequest(
    @field:NotBlank(message = "이름은 필수입니다.")
    val name: String,
    @field:Email
    @field:NotBlank
    val email: String,
    @field:NotBlank(message = "비밀번호는 필수입니다.")
    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @field:Size(max = 64, message = "비밀번호는 최대 64자 이하여야 합니다.")
    val password: String,
)