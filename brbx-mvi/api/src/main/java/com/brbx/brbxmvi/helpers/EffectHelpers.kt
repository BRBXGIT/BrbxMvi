package com.brbx.brbxmvi.helpers

import com.brbx.brbxmvi.contracts.MviDelegate

/**
 * Posts an effect via the [MviDelegate]'s scope.
 */
fun <E> MviDelegate<*, E, *>.postEffect(
    effect: E,
) = scope.postEffect(effect)

/**
 * Conditionally posts an effect.
 */
@MviApiWithIf
fun <E> MviDelegate<*, E, *>.postEffectIf(
    effect: E,
    condition: Boolean,
    onElse: () -> Unit = {},
): Unit? = if (condition) postEffect(effect) else {
    onElse()
    null
}