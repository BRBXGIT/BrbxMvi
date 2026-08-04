package com.brbx.api.helpers

import com.brbx.api.contracts.MviDelegate
import kotlin.properties.ReadOnlyProperty

val <S> MviDelegate<S, *, *>.currentState: S
    get() = scope.state.value

fun <S, E, I : Any, T> MviDelegate<S, E, I>.select(
    selector: (S) -> T
): ReadOnlyProperty<Any?, T> = ReadOnlyProperty { _, _ ->
    selector(currentState)
}

inline fun <S, E, I : Any, R> MviDelegate<S, E, I>.withState(
    block: (state: S) -> R
): R = block(currentState)

fun <S, E, I : Any> MviDelegate<S, E, I>.reduce(
    reducer: S.() -> S
) {
    scope.reduce(reducer)
}

fun <S, E, I : Any> MviDelegate<S, E, I>.reduceIf(
    condition: Boolean,
    reducer: S.() -> S
) {
    if (condition) reduce(reducer)
}

inline fun <reified T : S, S, E, I : Any> MviDelegate<S, E, I>.reduceIfType(
    crossinline reducer: T.() -> S
) {
    val state = currentState
    if (state is T) {
        reduce { reducer(state) }
    }
}