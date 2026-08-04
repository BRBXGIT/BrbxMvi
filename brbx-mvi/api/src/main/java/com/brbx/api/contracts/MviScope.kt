package com.brbx.api.contracts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MviScope<State, Effect, in Intent : Any> {
    val viewModelScope: CoroutineScope
    val state: StateFlow<State>
    val effects: SharedFlow<Effect>

    fun reduce(reducer: State.() -> State)
    fun dispatchIntent(intent: Intent)
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