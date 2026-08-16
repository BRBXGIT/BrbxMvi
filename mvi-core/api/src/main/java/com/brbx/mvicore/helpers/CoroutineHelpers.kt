package com.brbx.mvicore.helpers

import com.brbx.mvicore.contracts.MviDelegate
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Launches a coroutine within the [MviDelegate]'s viewModelScope.
 *
 * This helper simplifies launching asynchronous actions from delegates.
 */
inline fun <S, E, I : Any> MviDelegate<S, E, I>.launchAction(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline block: suspend () -> Unit,
): Job = scope.viewModelScope.launch(context, start) { block() }

/**
 * Conditionally launches a coroutine.
 *
 * If [condition] is true, [block] is executed. Otherwise, [onElse] is called.
 */
inline fun <S, E, I : Any> MviDelegate<S, E, I>.launchActionIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    crossinline block: suspend () -> Unit,
): Job? = if (condition) launchAction(context, start, block) else {
    onElse()
    null
}

/**
 * Creates a [Deferred] value within the [MviDelegate]'s viewModelScope.
 *
 * Use this when you need to compute a value asynchronously and await its result.
 */
inline fun <S, E, I : Any, T> MviDelegate<S, E, I>.asyncAction(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline block: suspend () -> T,
): Deferred<T> = scope.viewModelScope.async(context, start) { block() }

/**
 * Conditionally creates a [Deferred] value.
 */
inline fun <S, E, I : Any, T> MviDelegate<S, E, I>.asyncActionIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    crossinline block: suspend () -> T,
): Deferred<T>? = if (condition) asyncAction(context, start, block) else {
    onElse()
    null
}