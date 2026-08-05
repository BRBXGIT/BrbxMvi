package com.brbx.api.helpers

/**
 * An annotation used to mark MVI APIs that provide conditional execution (e.g., suffixed with `If`).
 */
@RequiresOptIn(
    message = "This is experimental api can be removed in future releases",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class MviApiWithIf