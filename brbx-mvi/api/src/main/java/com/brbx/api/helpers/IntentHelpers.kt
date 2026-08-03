package com.brbx.api.helpers

import com.brbx.api.contracts.BrbxMviDelegate

fun <I : Any> BrbxMviDelegate<*, *, I>.dispatchIntent(intent: I) {
    scope.dispatchIntent(intent)
}

fun <I : Any> BrbxMviDelegate<*, *, I>.dispatchIntentIf(intent: I, condition: Boolean) {
    if (condition) dispatchIntent(intent)
}