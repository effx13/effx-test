package one.effx.api.auth.dto

data class AuthLoginResponse(
    val refreshToken: String,
    val accessToken: String,
)