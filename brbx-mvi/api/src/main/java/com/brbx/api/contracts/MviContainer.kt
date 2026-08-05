package com.brbx.api.contracts

/**
 * An interface for objects that contain an [MviScope].
 *
 * Typically implemented by ViewModels or other components that manage an MVI lifecycle,
 * allowing them to expose the scope to external observers or delegates.
 */
interface MviContainer<State, Effect, in Intent : Any> {
    /**
     * The [MviScope] associated with this container.
     */
    val scope: MviScope<State, Effect, Intent>
}