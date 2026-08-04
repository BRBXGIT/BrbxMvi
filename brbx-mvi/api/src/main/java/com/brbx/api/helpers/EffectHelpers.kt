package com.brbx.api.helpers

import com.brbx.api.contracts.MviDelegate

fun <E> MviDelegate<*, E, *>.postEffect(
    effect: E,
) = scope.postEffect(effect)

fun <E> MviDelegate<*, E, *>.postEffectIf(
    effect: E,
    condition: Boolean,
): Unit? = if (condition) postEffect(effect) else null