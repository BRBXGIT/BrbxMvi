package com.brbx.api.helpers

import com.brbx.api.contracts.MviDelegate

/**
 * Dispatches an intent via the [MviDelegate]'s scope.
 */
fun <I : Any> MviDelegate<*, *, I>.dispatchIntent(intent: I) {
    scope.dispatchIntent(intent)
}

/**
 * Conditionally dispatches an intent.
 */
@MviApiWithIf
fun <I : Any> MviDelegate<*, *, I>.dispatchIntentIf(
    intent: I,
    condition: Boolean,
    onElse: () -> Unit = {},
) {
    if (condition) dispatchIntent(intent) else onElse()
}