package com.brbx.api.helpers

import com.brbx.api.contracts.BrbxMviDelegate

fun <E> BrbxMviDelegate<*, E, *>.postEffect(
    effect: E,
) = scope.postEffect(effect)

fun <E> BrbxMviDelegate<*, E, *>.postEffectIf(
    effect: E,
    condition: Boolean,
): Unit? = if (condition) postEffect(effect) else null