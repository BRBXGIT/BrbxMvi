package com.brbx.api.helpers

@RequiresOptIn(
    message = "This is experimental api can be removed in future releases",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class MviApiWithIf