package one.effx.domain.member.exception

import one.effx.common.exception.EffxException


class MemberException(
    private val statusCode: Int,
    private val error: String,
    override val message: String,
): EffxException(message) {
    override fun statusCode() = statusCode

    override fun errorCode() = error

    companion object {
        fun emailAlreadyExists(): MemberException {
            return MemberException(
                statusCode = 409,
                error = "EMAIL_ALREADY_EXISTS",
                message = "이미 존재하는 이메일입니다."
            )
        }
    }
}