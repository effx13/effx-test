package one.effx.api.annotation

import io.swagger.v3.oas.annotations.Hidden

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@Hidden
annotation class MemberPrincipal
