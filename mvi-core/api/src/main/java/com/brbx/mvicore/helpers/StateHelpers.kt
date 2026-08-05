package com.brbx.mvicore.helpers

import com.brbx.mvicore.contracts.MviDelegate
import kotlin.properties.ReadOnlyProperty

/**
 * Provides quick access to the current state value from the [MviDelegate].
 */
val <S> MviDelegate<S, *, *>.currentState: S
    get() = scope.state.value

/**
 * Creates a read-only property that selects a portion of the state.
 *
 * Useful for exposing specific state fields in a clean way.
 */
fun <S, E, I : Any, T> MviDelegate<S, E, I>.select(
    selector: (S) -> T
): ReadOnlyProperty<Any?, T> = ReadOnlyProperty { _, _ ->
    selector(currentState)
}

/**
 * Executes a block of code with the current state.
 */
inline fun <S, E, I : Any, R> MviDelegate<S, E, I>.withState(
    block: (state: S) -> R
): R = block(currentState)

/**
 * Performs a state reduction via the [MviDelegate]'s scope.
 */
fun <S, E, I : Any> MviDelegate<S, E, I>.reduce(
    reducer: S.() -> S
) {
    scope.reduce(reducer)
}

/**
 * Conditionally performs a state reduction.
 */
@MviApiWithIf
fun <S, E, I : Any> MviDelegate<S, E, I>.reduceIf(
    condition: Boolean,
    onElse: () -> Unit = {},
    reducer: S.() -> S
) {
    if (condition) reduce(reducer) else onElse()
}

/**
 * Performs a state reduction only if the current state is of type [T].
 *
 * Particularly useful when dealing with sealed classes for State (e.g., Loading, Success, Error).
 */
inline fun <reified T : S, S, E, I : Any> MviDelegate<S, E, I>.reduceIfType(
    crossinline reducer: T.() -> S
) {
    reduce {
        if (this is T) reducer(this) else this
    }
}