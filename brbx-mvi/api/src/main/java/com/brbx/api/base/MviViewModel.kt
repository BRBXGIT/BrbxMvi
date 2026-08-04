package com.brbx.api.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brbx.api.contracts.MviScope
import com.brbx.api.helpers.shareInLazily
import com.brbx.api.helpers.stateInLazily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<State, Effect, in Intent : Any>(
    initialState: State,
    effectReplay: Int = 1,
) : ViewModel() {

    protected open val _state = MutableStateFlow(value = initialState)
    protected open val _effects = MutableSharedFlow<Effect>(replay = effectReplay)

    open val state = _state.stateInLazily(initialValue = initialState)
    open val effects = _effects.shareInLazily()

    protected open fun reduce(reducer: State.() -> State) {
        _state.update(function = reducer)
    }

    protected open fun mviScope(): MviScope<State, Effect, Intent> =
        object : MviScope<State, Effect, Intent> {
            override val viewModelScope: CoroutineScope = this@MviViewModel.viewModelScope
            override val state: StateFlow<State> = this@MviViewModel.state
            override val effects: SharedFlow<Effect> = this@MviViewModel.effects

            override fun reduce(reducer: State.() -> State) {
                this@MviViewModel.reduce(reducer)
            }

            override fun dispatchIntent(intent: Intent) {
                this@MviViewModel.dispatchIntent(intent)
            }

            override fun postEffect(effect: Effect) {
                this@MviViewModel.postEffect(effect)
            }
        }

    open fun dispatchIntent(intent: Intent) {}

    open fun postEffect(effect: Effect) {
        viewModelScope.launch { _effects.emit(value = effect) }
    }
}