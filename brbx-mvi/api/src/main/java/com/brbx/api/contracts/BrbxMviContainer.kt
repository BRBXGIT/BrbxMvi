package com.brbx.api.contracts

interface BrbxMviContainer<State, Effect, in Intent : Any> {
    val scope: MviScope<State, Effect, Intent>
}