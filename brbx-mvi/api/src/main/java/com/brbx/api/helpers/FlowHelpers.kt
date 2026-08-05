package com.brbx.api.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brbx.api.contracts.MviDelegate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Converts a [Flow] to a [StateFlow] that starts eagerly in the [ViewModel]'s scope.
 *
 * This is a shorthand for `stateIn(viewModelScope, SharingStarted.Eagerly, initialValue)`.
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.stateInEagerly(initialValue: T): StateFlow<T> =
    this.stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = initialValue,
    )

/**
 * Converts a [Flow] to a [StateFlow] that starts lazily in the [ViewModel]'s scope.
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.stateInLazily(initialValue: T): StateFlow<T> =
    this.stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue,
    )

/**
 * Converts a [Flow] to a [StateFlow] that remains active while there are subscribers.
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.stateInWhileSubscribed(
    initialValue: T,
    stopTimeoutMillis: Long = 1L,
): StateFlow<T> =
    this.stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = stopTimeoutMillis),
        initialValue = initialValue,
    )

/**
 * Converts a [Flow] to a [SharedFlow] that starts eagerly in the [ViewModel]'s scope.
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.shareInEagerly(): SharedFlow<T> =
    this.shareIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Eagerly,
    )

/**
 * Converts a [Flow] to a [SharedFlow] that starts lazily in the [ViewModel]'s scope.
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.shareInLazily(): SharedFlow<T> =
    this.shareIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Lazily,
    )

/**
 * Converts a [Flow] to a [SharedFlow] that remains active while there are subscribers.
 */
context(viewModel: ViewModel)
fun <T> Flow<T>.shareInWhileSubscribed(stopTimeoutMillis: Long = 1L): SharedFlow<T> =
    this.shareIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = stopTimeoutMillis),
    )

/**
 * Collects values from the [Flow] and executes the [action] within the [MviDelegate]'s context.
 *
 * This allows for easy integration of external flows into the MVI loop.
 */
context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectFlow(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job =
    delegate.launchAction(context = context) {
        this@collectFlow.collect { value ->
            delegate.action(value)
        }
    }

/**
 * Conditionally collects values from the [Flow].
 */
@MviApiWithIf
context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectFlowIf(
    condition: Boolean,
    onElse: () -> Unit = {},
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job? =
    if (condition) {
        collectFlow(context, action)
    } else {
        onElse()
        null
    }

/**
 * Binds the [Flow] to state updates. For every new value in the flow, the [reducer] is called
 * to update the MVI state.
 */
context(delegate: MviDelegate<S, E, I>)
inline infix fun <S, E, I : Any, T> Flow<T>.bind(
    crossinline reducer: S.(T) -> S,
): Job =
    collectFlow { value ->
        reduce {
            reducer(value)
        }
    }

/**
 * Conditionally binds the [Flow] to state updates.
 */
@MviApiWithIf
context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.bindIf(
    condition: Boolean,
    onElse: () -> Unit = {},
    crossinline reducer: S.(T) -> S,
): Job? =
    if (condition) {
        bind(reducer)
    } else {
        onElse()
        null
    }

/**
 * Collects only the latest value from the [Flow], cancelling any previous collection jobs.
 */
context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectFlowLatest(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job =
    delegate.launchAction(context = context) {
        this@collectFlowLatest.collectLatest { value ->
            delegate.action(value)
        }
    }

/**
 * Conditionally collects the latest value from the [Flow].
 */
@MviApiWithIf
context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectFlowLatestIf(
    condition: Boolean,
    onElse: () -> Unit = {},
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job? =
    if (condition) {
        collectFlow(context, action)
    } else {
        onElse()
        null
    }

/**
 * Binds the [Flow] to state updates using [collectLatest].
 */
context(delegate: MviDelegate<S, E, I>)
inline infix fun <S, E, I : Any, T> Flow<T>.bindLatest(
    crossinline reducer: S.(T) -> S,
): Job =
    collectFlowLatest { value ->
        reduce {
            reducer(value)
        }
    }

/**
 * Conditionally binds the [Flow] to state updates using [collectLatest].
 */
@MviApiWithIf
context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.bindLatestIf(
    condition: Boolean,
    onElse: () -> Unit = {},
    crossinline reducer: S.(T) -> S,
): Job? =
    if (condition) {
        bindLatest(reducer)
    } else {
        onElse()
        null
    }

/**
 * Selects a portion of the state and returns it as a [Flow] that emits only when the selected
 * value changes.
 */
fun <S, E, I : Any, T> MviDelegate<S, E, I>.selectFlow(
    selector: (S) -> T
): Flow<T> = scope.state.map(transform = selector).distinctUntilChanged()