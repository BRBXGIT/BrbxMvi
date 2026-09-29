package com.brbx.mvi_core.helpers

import com.brbx.mvi_core.contracts.MviDelegate

/**
 * Posts an effect via the [MviDelegate]'s scope.
 */
fun <E> MviDelegate<*, E, *, *>.postEffect(
    effect: E,
) = mviScope.postEffect(effect)

/**
 * Conditionally posts an effect.
 */
fun <E> MviDelegate<*, E, *, *>.postEffectIf(
    effect: E,
    condition: Boolean,
    onElse: () -> Unit = {},
): Unit? = if (condition) postEffect(effect) else {
    onElse()
    null
}

/**
 * Posts a screen effect via the [MviDelegate]'s scope.
 */
fun <SE> MviDelegate<*, *, SE, *>.postScreenEffect(
    effect: SE,
) = mviScope.postScreenEffect(effect)

/**
 * Conditionally posts a screen effect.
 */
fun <SE> MviDelegate<*, *, SE, *>.postScreenEffectIf(
    effect: SE,
    condition: Boolean,
    onElse: () -> Unit = {},
): Unit? = if (condition) postScreenEffect(effect) else {
    onElse()
    null
}