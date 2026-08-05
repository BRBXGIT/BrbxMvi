package com.brbx.brbxmvi.helpers

import com.brbx.brbxmvi.contracts.MviDelegate
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
    crossinline block: suspend () -> Unit,
): Job = scope.viewModelScope.launch(context = context) { block() }

/**
 * Conditionally launches a coroutine.
 *
 * If [condition] is true, [block] is executed. Otherwise, [onElse] is called.
 */
@MviApiWithIf
inline fun <S, E, I : Any> MviDelegate<S, E, I>.launchActionIf(
    condition: Boolean,
    onElse: () -> Unit = {},
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> Unit,
): Job? = if (condition) launchAction(context, block) else {
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
    crossinline block: suspend () -> T,
): Deferred<T> = scope.viewModelScope.async(context = context) { block() }

/**
 * Conditionally creates a [Deferred] value.
 */
@MviApiWithIf
inline fun <S, E, I : Any, T> MviDelegate<S, E, I>.asyncActionIf(
    condition: Boolean,
    onElse: () -> Unit = {},
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
): Deferred<T>? = if (condition) asyncAction(context, block) else {
    onElse()
    null
}