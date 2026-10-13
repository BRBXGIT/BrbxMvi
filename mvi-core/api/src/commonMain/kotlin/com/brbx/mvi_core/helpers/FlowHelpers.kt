package com.brbx.mvi_core.helpers

import com.brbx.mvi_core.contracts.MviDelegate
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Collects values from the [Flow] and executes the [action] within the [MviDelegate]'s context.
 *
 * This allows for easy integration of external flows into the MVI loop.
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.collectFlow(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline action: suspend MviDelegate<S, E, SE, I>.(T) -> Unit
): Job =
    delegate.launchAction(context, start) {
        this@collectFlow.collect { value ->
            delegate.action(value)
        }
    }

/**
 * Conditionally collects values from the [Flow].
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.collectFlowIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    crossinline action: suspend MviDelegate<S, E, SE, I>.(T) -> Unit
): Job? =
    if (condition) {
        collectFlow(context, start, action)
    } else {
        onElse()
        null
    }

/**
 * Binds the [Flow] to state updates. For every new value in the flow, the [reducer] is called
 * to update the MVI state.
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline infix fun <S, E, SE, I : Any, T> Flow<T>.bind(
    crossinline reducer: S.(T) -> S,
): Job =
    collectFlow { value ->
        reduce {
            reducer(value)
        }
    }

context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.bind(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline reducer: S.(T) -> S,
): Job =
    collectFlow(context, start) { value ->
        reduce {
            reducer(value)
        }
    }

/**
 * Conditionally binds the [Flow] to state updates.
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.bindIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    crossinline reducer: S.(T) -> S,
): Job? =
    if (condition) {
        bind(context, start, reducer)
    } else {
        onElse()
        null
    }

/**
 * Collects only the latest value from the [Flow], cancelling any previous collection jobs.
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.collectFlowLatest(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline action: suspend MviDelegate<S, E, SE, I>.(T) -> Unit
): Job =
    delegate.launchAction(context, start) {
        this@collectFlowLatest.collectLatest { value ->
            delegate.action(value)
        }
    }

/**
 * Conditionally collects the latest value from the [Flow].
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.collectFlowLatestIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    crossinline action: suspend MviDelegate<S, E, SE, I>.(T) -> Unit
): Job? =
    if (condition) {
        collectFlow(context, start, action)
    } else {
        onElse()
        null
    }

/**
 * Binds the [Flow] to state updates using [collectLatest].
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline infix fun <S, E, SE, I : Any, T> Flow<T>.bindLatest(
    crossinline reducer: S.(T) -> S,
): Job =
    collectFlowLatest { value ->
        reduce {
            reducer(value)
        }
    }

context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.bindLatest(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline reducer: S.(T) -> S,
): Job =
    collectFlowLatest(context, start) { value ->
        reduce {
            reducer(value)
        }
    }

/**
 * Conditionally binds the [Flow] to state updates using [collectLatest].
 */
context(delegate: MviDelegate<S, E, SE, I>)
inline fun <S, E, SE, I : Any, T> Flow<T>.bindLatestIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    crossinline reducer: S.(T) -> S,
): Job? =
    if (condition) {
        bindLatest(context, start, reducer)
    } else {
        onElse()
        null
    }

/**
 * Selects a portion of the state and returns it as a [Flow] that emits only when the selected
 * value changes.
 */
fun <S, E, SE, I : Any, T> MviDelegate<S, E, SE, I>.selectFlow(
    selector: (S) -> T
): Flow<T> = mviScope.state.map(transform = selector).distinctUntilChanged()