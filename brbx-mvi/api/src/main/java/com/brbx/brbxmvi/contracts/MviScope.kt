package com.brbx.brbxmvi.contracts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A scope that provides all necessary tools to interact with an MVI component.
 *
 * It serves as a bridge between the ViewModel (the producer) and delegates or UI (the consumers/logic providers).
 */
interface MviScope<State, Effect, in Intent : Any> {
    /**
     * The [CoroutineScope] tied to the ViewModel's lifecycle.
     */
    val viewModelScope: CoroutineScope

    /**
     * A [StateFlow] of the current state.
     */
    val state: StateFlow<State>

    /**
     * A [SharedFlow] of effects.
     */
    val effects: SharedFlow<Effect>

    /**
     * Updates the state via a reducer function.
     */
    fun reduce(reducer: State.() -> State)

    /**
     * Dispatches an intent to the MVI system.
     */
    fun dispatchIntent(intent: Intent)

    /**
     * Posts a one-time effect.
     */
    fun postEffect(effect: Effect)
}

private class DefaultMviScope<State, Effect, in Intent : Any>(
    override val viewModelScope: CoroutineScope,
    override val state: StateFlow<State>,
    override val effects: SharedFlow<Effect>,
    private val onReduce: (State.() -> State) -> Unit,
    private val onDispatchIntent: (Intent) -> Unit,
    private val onPostEffect: (Effect) -> Unit,
) : MviScope<State, Effect, Intent> {
    override fun reduce(reducer: State.() -> State) {
        onReduce(reducer)
    }

    override fun dispatchIntent(intent: Intent) {
        onDispatchIntent(intent)
    }

    override fun postEffect(effect: Effect) {
        onPostEffect(effect)
    }
}

/**
 * Factory function to create an [MviScope] from individual components.
 * Useful for custom ViewModel implementations or when delegating MVI logic.
 */
fun <State, Effect, Intent : Any> ViewModel.mviScope(
    state: StateFlow<State>,
    effects: SharedFlow<Effect>,
    reduce: (State.() -> State) -> Unit,
    dispatchIntent: (Intent) -> Unit,
    postEffect: (Effect) -> Unit,
) : MviScope<State, Effect, Intent> =
    DefaultMviScope(
        viewModelScope = viewModelScope,
        state = state,
        effects = effects,
        onReduce = reduce,
        onDispatchIntent = dispatchIntent,
        onPostEffect = postEffect,
    )