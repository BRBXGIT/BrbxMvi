package com.brbx.mvicore.base

import com.brbx.mvicore.contracts.MviContainer
import com.brbx.mvicore.contracts.MviScope

/**
 * A specialized [MviViewModel] that implements [MviContainer].
 *
 * This class is used when the ViewModel itself serves as the container for the MVI logic,
 * providing its own [MviScope] to observers or delegates.
 */
abstract class ContainedMviViewModel<State, Effect, in Intent : Any>(
    initialState: State,
    effectReplay: Int = 1,
) : MviViewModel<State, Effect, Intent>(initialState, effectReplay),
    MviContainer<State, Effect, Intent> {

    /**
     * The [MviScope] provided by this ViewModel, allowing external components to interact
     * with its state, effects, and intent dispatching.
     */
    override val scope: MviScope<State, Effect, Intent> = mviScope()
}