package com.brbx.api.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brbx.api.contracts.BrbxMviContainer
import com.brbx.api.contracts.MviScope
import com.brbx.api.contracts.mviScope
import com.brbx.api.helpers.shareInLazily
import com.brbx.api.helpers.stateInLazily
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BrbxViewModel<State, Effect, in Intent : Any>(
    initialState: State,
    effectReplay: Int = 1,
) : ViewModel(), BrbxMviContainer<State, Effect, Intent> {

    protected open val _state = MutableStateFlow(value = initialState)
    open val state = _state.stateInLazily(initialValue = initialState)

    protected open val _effects = MutableSharedFlow<Effect>(replay = effectReplay)
    open val effects = _effects.shareInLazily()

    override val scope: MviScope<State, Effect, Intent> = mviScope(
        reduce = { r -> reduce(r) },
    )

    protected open fun reduce(reducer: State.() -> State) {
        _state.update(function = reducer)
    }

    open fun dispatchIntent(intent: Intent) {}

    open fun postEffect(effect: Effect) {
        viewModelScope.launch { _effects.emit(value = effect) }
    }
}