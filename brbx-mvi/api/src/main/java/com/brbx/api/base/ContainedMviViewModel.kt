package com.brbx.api.base

import com.brbx.api.contracts.MviContainer
import com.brbx.api.contracts.MviScope

abstract class ContainedMviViewModel<State, Effect, in Intent : Any>(
    initialState: State,
    effectReplay: Int = 1,
) : MviViewModel<State, Effect, Intent>(initialState, effectReplay),
    MviContainer<State, Effect, Intent> {

    override val scope: MviScope<State, Effect, Intent> = mviScope()
}