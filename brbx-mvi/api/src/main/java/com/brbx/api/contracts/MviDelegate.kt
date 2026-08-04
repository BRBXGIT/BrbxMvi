package com.brbx.api.contracts

interface MviDelegate<State, Effect, in Intent : Any> {
    val scope: MviScope<State, Effect, Intent>

    fun process(intent: Intent)
}