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

internal class TestMviDelegate<S, E, SE, I : Any>(
    override val scope: TestMviScope<S, E, SE, I>
) : MviDelegate<S, E, SE, I> {

    constructor(
        initialState: S,
        viewModelScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    ) : this(TestMviScope(initialState, viewModelScope))

    val intents = mutableListOf<I>()

    override fun invoke(intent: I) {
        intents.add(intent)
    }

    internal class TestMviScope<S, E, SE, I : Any>(
        initialState: S,
        override val viewModelScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    ) : MviScope<S, E, SE, I> {

        private val _state = MutableStateFlow(initialState)
        override val state: StateFlow<S> = _state.asStateFlow()

        private val _effects = MutableSharedFlow<E>()
        override val effects: SharedFlow<E> = _effects.asSharedFlow()

        private val _screenEffects = MutableSharedFlow<SE>()
        override val screenEffects: SharedFlow<SE> = _screenEffects.asSharedFlow()

        val dispatchedIntents = mutableListOf<I>()
        val postedEffects = mutableListOf<E>()
        val postedScreenEffects = mutableListOf<SE>()

        override fun reduce(reducer: S.() -> S) {
            _state.value = reducer(_state.value)
        }

        override fun dispatchIntent(intent: I) {
            dispatchedIntents.add(intent)
        }

        override fun postEffect(effect: E) {
            postedEffects.add(effect)
        }

        override fun postScreenEffect(effect: SE) {
            postedScreenEffects.add(effect)
        }
    }
}
