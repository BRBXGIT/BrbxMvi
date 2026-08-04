package com.brbx.api.helpers

import com.brbx.api.contracts.MviDelegate

fun <E> MviDelegate<*, E, *>.postEffect(
    effect: E,
) = scope.postEffect(effect)

@MviApiWithIf
fun <E> MviDelegate<*, E, *>.postEffectIf(
    effect: E,
    condition: Boolean,
    onElse: () -> Unit = {},
): Unit? = if (condition) postEffect(effect) else {
    onElse()
    null
}