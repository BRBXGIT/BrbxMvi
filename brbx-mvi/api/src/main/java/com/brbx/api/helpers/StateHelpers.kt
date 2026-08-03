package com.brbx.api.helpers

import com.brbx.api.contracts.BrbxMviDelegate

val <S> BrbxMviDelegate<S, *, *>.currentState: S
    get() = scope.state.value

inline fun <S, E, I : Any, R> BrbxMviDelegate<S, E, I>.withState(
    block: (state: S) -> R
): R = block(currentState)

fun <S, E, I : Any> BrbxMviDelegate<S, E, I>.reduce(
    reducer: S.() -> S
) {
    scope.reduce(reducer)
}

fun <S, E, I : Any> BrbxMviDelegate<S, E, I>.reduceIf(
    condition: Boolean,
    reducer: S.() -> S
) {
    if (condition) reduce(reducer)
}

inline fun <reified T : S, S, E, I : Any> BrbxMviDelegate<S, E, I>.reduceIfType(
    crossinline reducer: T.() -> S
) {
    val state = currentState
    if (state is T) {
        reduce { reducer(state) }
    }
}