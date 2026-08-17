package com.brbx.mvicore.contracts

/**
 * An interface for components that delegate MVI logic.
 *
 * Unlike [MviContainer], a delegate is expected to actively participate in the MVI loop
 * by providing a [invoke] method to handle intents.
 */
interface MviDelegate<State, Effect, in Intent : Any> {
    /**
     * The [MviScope] this delegate operates within.
     */
    val scope: MviScope<State, Effect, Intent>

    /**
     * Processes a specific [intent]. This is often the entry point for business logic
     * triggered by user actions or system events.
     */
    operator fun invoke(intent: Intent)
}