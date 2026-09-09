package com.brbx.mvi_core.helpers

import com.brbx.mvi_core.contracts.MviDelegate
import com.brbx.mvi_core.contracts.MviScope

/**
 * Dispatches an intent via the [MviDelegate]'s scope.
 */
@Suppress("UNCHECKED_CAST")
fun <I : Any> MviDelegate<*, *, *, *>.dispatchIntent(intent: I) {
    (scope as MviScope<*, *, *, I>).dispatchIntent(intent)
}

/**
 * Conditionally dispatches an intent.
 */
fun <I : Any> MviDelegate<*, *, *, *>.dispatchIntentIf(
    intent: I,
    condition: Boolean,
    onElse: () -> Unit = {},
) {
    if (condition) dispatchIntent(intent) else onElse()
}