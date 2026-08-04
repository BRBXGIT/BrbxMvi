package com.brbx.api.helpers

import com.brbx.api.contracts.MviDelegate

fun <I : Any> MviDelegate<*, *, I>.dispatchIntent(intent: I) {
    scope.dispatchIntent(intent)
}

fun <I : Any> MviDelegate<*, *, I>.dispatchIntentIf(intent: I, condition: Boolean) {
    if (condition) dispatchIntent(intent)
}