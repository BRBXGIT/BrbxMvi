package com.brbx.mvicore.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brbx.mvicore.contracts.MviScope
import com.brbx.mvicore.helpers.shareInLazily
import com.brbx.mvicore.helpers.stateInLazily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A base class for [ViewModel]s that follow the MVI (Model-View-Intent) pattern.
 *
 * It manages the lifecycle of [State] and [Effect] streams, providing a structured way
 * to handle state updates and one-time events (effects).
 *
 * @param initialState The initial state of the ViewModel.
 * @param effectReplay The number of effects to replay to new subscribers.
 */
abstract class MviViewModel<State, Effect, ScreenEffect, in Intent : Any>(
    initialState: State,
    effectReplay: Int = 1,
    screenEffectReplay: Int = 1,
) : ViewModel() {

    /**
     * Internal mutable state flow.
     */
    protected open val _state = MutableStateFlow(value = initialState)

    /**
     * Internal mutable common effects flow.
     */
    protected open val _effects = MutableSharedFlow<Effect>(replay = effectReplay)

    /**
     * Internal mutable screen effects flow.
     */
    protected open val _screenEffects = MutableSharedFlow<ScreenEffect>(replay = screenEffectReplay)

    /**
     * Public read-only [StateFlow] of the current state.
     * Uses [stateInLazily] to ensure the flow is active only when needed.
     */
    open val state = _state.stateInLazily(initialValue = initialState)

    /**
     * Public read-only [SharedFlow] of effects.
     * Uses [shareInLazily] to manage effect distribution.
     */
    open val effects = _effects.shareInLazily()

    /**
     * Public read-only [SharedFlow] of screen effects.
     * Uses [shareInLazily] to manage effect distribution.
     */
    open val screenEffects = _screenEffects.shareInLazily()

    /**
     * Updates the current state using the provided [reducer].
     * This is the primary way to perform state transitions.
     */
    protected open fun reduce(reducer: State.() -> State) {
        _state.update(function = reducer)
    }

    /**
     * Creates an [MviScope] that wraps this ViewModel's functionality.
     * This scope can be passed to delegates or other components to allow them
     * to interact with the MVI loop.
     */
    protected open fun mviScope(): MviScope<State, Effect, ScreenEffect, Intent> =
        object : MviScope<State, Effect, ScreenEffect, Intent> {
            override val viewModelScope: CoroutineScope = this@MviViewModel.viewModelScope
            override val state: StateFlow<State> = this@MviViewModel.state
            override val effects: SharedFlow<Effect> = this@MviViewModel.effects
            override val screenEffects: SharedFlow<ScreenEffect> = this@MviViewModel.screenEffects

            override fun reduce(reducer: State.() -> State) {
                this@MviViewModel.reduce(reducer)
            }

            override fun dispatchIntent(intent: Intent) {
                this@MviViewModel.dispatchIntent(intent)
            }

            override fun postEffect(effect: Effect) {
                this@MviViewModel.postEffect(effect)
            }

            override fun postScreenEffect(effect: ScreenEffect) {
                this@MviViewModel.postScreenEffect(effect)
            }
        }

    /**
     * Dispatches an [Intent] to be processed by the ViewModel.
     * Subclasses should override this to handle specific business logic.
     */
    open fun dispatchIntent(intent: Intent) {}

    /**
     * Posts a one-time [Effect] to be consumed by the UI.
     */
    open fun postEffect(effect: Effect) {
        viewModelScope.launch { _effects.emit(value = effect) }
    }

    /**
     * Posts a one-time [ScreenEffect] to be consumed by the UI.
     */
    open fun postScreenEffect(effect: ScreenEffect) {
        viewModelScope.launch { _screenEffects.emit(value = effect) }
    }
}