package com.brbx.mvicore.helpers

import org.junit.Assert.assertEquals
import org.junit.Test

internal class StateHelpersTest {

    internal data class TestState(val count: Int = 0, val text: String = "")

    @Test
    fun `currentState returns actual state from scope`() {
        val delegate = TestMviDelegate<TestState, Unit, Any>(TestState(count = 5))
        assertEquals(5, delegate.currentState.count)
    }

    @Test
    fun `select delegate to currentState property`() {
        val delegate = TestMviDelegate<TestState, Unit, Any>(TestState(count = 10))
        val count by delegate.select { it.count }
        
        assertEquals(10, count)
        
        delegate.reduce { copy(count = 20) }
        assertEquals(20, count)
    }

    @Test
    fun `withState provides current state to block`() {
        val delegate = TestMviDelegate<TestState, Unit, Any>(TestState(count = 7))
        val result = delegate.withState { it.count * 2 }
        assertEquals(14, result)
    }

    @Test
    fun `reduce updates state in scope`() {
        val delegate = TestMviDelegate<TestState, Unit, Any>(TestState(count = 0))
        delegate.reduce { copy(count = 1) }
        assertEquals(1, delegate.currentState.count)
    }

    @Test
    fun `reduceIf updates state only when condition is true`() {
        val delegate = TestMviDelegate<TestState, Unit, Any>(TestState(count = 0))
        
        delegate.reduceIf(condition = false) { copy(count = 1) }
        assertEquals(0, delegate.currentState.count)
        
        delegate.reduceIf(condition = true) { copy(count = 2) }
        assertEquals(2, delegate.currentState.count)
    }

    internal interface State
    internal data class TypeA(val valA: String) : State
    internal data class TypeB(val valB: Int) : State

    @Test
    fun `reduceIfType updates state only when type matches`() {
        val delegate = TestMviDelegate<State, Unit, Any>(TypeA("hello"))
        
        delegate.reduceIfType<TypeB, State, Unit, Any> {
            TypeB(valB = 10)
        }
        assertEquals(TypeA("hello"), delegate.currentState)

        delegate.reduceIfType<TypeA, State, Unit, Any> {
            TypeA(valA = "world")
        }
        assertEquals(TypeA("world"), delegate.currentState)
    }
}
