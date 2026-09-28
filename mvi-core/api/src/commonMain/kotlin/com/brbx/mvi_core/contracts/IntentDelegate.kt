package com.brbx.mvi_core.contracts

/**
 * An interface for components that delegate MVI logic.
 *
 * Unlike [MviContainer], a delegate is expected to actively participate in the MVI loop
 * by providing a [invoke] method to handle intents.
 */
interface IntentDelegate<State, Effect, ScreenEffect, in Intent : Any>
    : MviDelegate<State, Effect, ScreenEffect, Intent> {

    /**
     * Processes a specific [intent]. This is often the entry point for business logic
     * triggered by user actions or system events.
     */
    operator fun invoke(intent: Intent)
}