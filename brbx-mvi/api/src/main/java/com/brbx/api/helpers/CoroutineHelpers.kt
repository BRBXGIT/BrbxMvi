package com.brbx.api.helpers

import com.brbx.api.contracts.BrbxMviDelegate
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

inline fun <S, E, I : Any> BrbxMviDelegate<S, E, I>.launchTask(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> Unit,
): Job = scope.viewModelScope.launch(context = context) { block() }

inline fun <S, E, I : Any> BrbxMviDelegate<S, E, I>.launchTaskIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> Unit,
): Job? = if (condition) launchTask(context, block) else null

inline fun <S, E, I : Any, T> BrbxMviDelegate<S, E, I>.asyncTask(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
): Deferred<T> = scope.viewModelScope.async(context = context) { block() }

inline fun <S, E, I : Any, T> BrbxMviDelegate<S, E, I>.asyncTaskIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
): Deferred<T>? = if (condition) asyncTask(context, block) else null