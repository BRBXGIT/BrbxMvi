package com.brbx.mvi_core.contracts

/**
 * An interface for components that delegate MVI logic.
 *
 * Unlike [MviContainer], a delegate is expected to actively participate in the MVI loop.
 */
interface MviDelegate<State, Effect, ScreenEffect, in Intent : Any> {
    /**
     * The [MviScope] this delegate operates within.
     */
    val mviScope: MviScope<State, Effect, ScreenEffect, Intent>
}