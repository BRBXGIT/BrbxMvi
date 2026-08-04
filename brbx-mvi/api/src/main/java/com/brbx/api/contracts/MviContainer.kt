package com.brbx.api.contracts

interface MviContainer<State, Effect, in Intent : Any> {
    val scope: MviScope<State, Effect, Intent>
}