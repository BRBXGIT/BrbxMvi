package com.brbx.mvi_core.helpers

import com.brbx.mvi_core.contracts.MviDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline val MviDelegate<*, *, *, *>.viewModelScope: CoroutineScope
    get() = mviScope.viewModelScope

/**
 * Launches a coroutine within the [MviDelegate]'s viewModelScope.
 *
 * This helper simplifies launching asynchronous actions from delegates.
 */
fun <S, E, SE, I : Any> MviDelegate<S, E, SE, I>.launchAction(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job = viewModelScope.launch(context, start, block)

/**
 * Conditionally launches a coroutine.
 *
 * If [condition] is true, [block] is executed. Otherwise, [onElse] is called.
 */
fun <S, E, SE, I : Any> MviDelegate<S, E, SE, I>.launchActionIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    block: suspend CoroutineScope.() -> Unit,
): Job? = if (condition) launchAction(context, start, block) else {
    onElse()
    null
}

/**
 * Creates a [Deferred] value within the [MviDelegate]'s viewModelScope.
 *
 * Use this when you need to compute a value asynchronously and await its result.
 */
fun <S, E, SE, I : Any, T> MviDelegate<S, E, SE, I>.asyncAction(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T,
): Deferred<T> = viewModelScope.async(context, start, block)

/**
 * Conditionally creates a [Deferred] value.
 */
fun <S, E, SE, I : Any, T> MviDelegate<S, E, SE, I>.asyncActionIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onElse: () -> Unit = {},
    block: suspend CoroutineScope.() -> T,
): Deferred<T>? = if (condition) asyncAction(context, start, block) else {
    onElse()
    null
}