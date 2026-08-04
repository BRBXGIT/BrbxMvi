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

context(viewModel: ViewModel)
fun <T> Flow<T>.stateInEagerly(initialValue: T): StateFlow<T> =
    this.stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = initialValue,
    )

context(viewModel: ViewModel)
fun <T> Flow<T>.stateInLazily(initialValue: T): StateFlow<T> =
    this.stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue,
    )

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

context(viewModel: ViewModel)
fun <T> Flow<T>.shareInEagerly(): SharedFlow<T> =
    this.shareIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Eagerly,
    )

context(viewModel: ViewModel)
fun <T> Flow<T>.shareInLazily(): SharedFlow<T> =
    this.shareIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.Lazily,
    )

context(viewModel: ViewModel)
fun <T> Flow<T>.shareInWhileSubscribed(stopTimeoutMillis: Long = 1L): SharedFlow<T> =
    this.shareIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = stopTimeoutMillis),
    )

context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectTask(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job =
    delegate.launchTask(context = context) {
        this@collectTask.collect { value ->
            delegate.action(value)
        }
    }

context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectTaskIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job? =
    if (condition) {
        collectTask(context, action)
    } else null

context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectLatestTask(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job =
    delegate.launchTask(context = context) {
        this@collectLatestTask.collectLatest { value ->
            delegate.action(value)
        }
    }

context(delegate: MviDelegate<S, E, I>)
inline fun <S, E, I : Any, T> Flow<T>.collectLatestTaskIf(
    condition: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: suspend MviDelegate<S, E, I>.(T) -> Unit
): Job? =
    if (condition) {
        collectTask(context, action)
    } else null

fun <S, E, I : Any, T> MviDelegate<S, E, I>.selectFlow(
    selector: (S) -> T
): Flow<T> = scope.state.map(transform = selector).distinctUntilChanged()