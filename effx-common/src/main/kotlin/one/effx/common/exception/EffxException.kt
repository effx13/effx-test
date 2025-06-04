package one.effx.common.exception

abstract class EffxException: RuntimeException {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable?): super(message, cause)

    abstract fun statusCode(): Int
    abstract fun errorCode(): String
}