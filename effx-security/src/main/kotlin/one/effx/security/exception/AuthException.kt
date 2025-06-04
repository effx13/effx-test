package one.effx.security.exception

import one.effx.common.exception.EffxException

class AuthException(
    private val statusCode: Int,
    private val error: String,
    override val message: String,
): EffxException(message) {
    override fun statusCode() = statusCode

    override fun errorCode() = error

    companion object {
        fun memberNotMatch(): AuthException {
            return AuthException(
                statusCode = 404,
                error = "MEMBER_NOT_MATCH",
                message = "존재하지 않는 사용자입니다."
            )
        }

        fun notLoggedIn(): AuthException {
            return AuthException(
                statusCode = 401,
                error = "NOT_LOGGED_IN",
                message = "로그인되지 않은 사용자입니다."
            )
        }

        fun invalidToken(): AuthException {
            return AuthException(
                statusCode = 401,
                error = "INVALID_TOKEN",
                message = "유효하지 않은 토큰입니다."
            )
        }
    }
}