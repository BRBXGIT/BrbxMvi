package com.brbx.mvicore.helpers

import com.brbx.mvicore.contracts.MviDelegate
import com.brbx.mvicore.contracts.MviScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.EmptyCoroutineContext

internal class TestMviDelegate<S, E, I : Any>(
    override val scope: TestMviScope<S, E, I>
) : MviDelegate<S, E, I> {

    constructor(
        initialState: S,
        viewModelScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    ) : this(TestMviScope(initialState, viewModelScope))

    val intents = mutableListOf<I>()

    override fun invoke(intent: I) {
        intents.add(intent)
    }

    internal class TestMviScope<S, E, I : Any>(
        initialState: S,
        override val viewModelScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    ) : MviScope<S, E, I> {

        private val _state = MutableStateFlow(initialState)
        override val state: StateFlow<S> = _state.asStateFlow()

        private val _effects = MutableSharedFlow<E>()
        override val effects: SharedFlow<E> = _effects.asSharedFlow()

        val dispatchedIntents = mutableListOf<I>()
        val postedEffects = mutableListOf<E>()

        override fun reduce(reducer: S.() -> S) {
            _state.value = reducer(_state.value)
        }

        override fun dispatchIntent(intent: I) {
            dispatchedIntents.add(intent)
        }

        override fun postEffect(effect: E) {
            postedEffects.add(effect)
            // Note: SharedFlow might not collect if not subscribed, 
            // but we track in postedEffects for verification
        }
    }
}
