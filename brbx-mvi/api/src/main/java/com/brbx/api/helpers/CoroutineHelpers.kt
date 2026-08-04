package com.brbx.api.helpers

import com.brbx.api.contracts.MviDelegate
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun <S, E, I : Any> MviDelegate<S, E, I>.launchAction(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> Unit,
): Job = scope.viewModelScope.launch(context = context) { block() }

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

inline fun <S, E, I : Any, T> MviDelegate<S, E, I>.asyncAction(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
): Deferred<T> = scope.viewModelScope.async(context = context) { block() }

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