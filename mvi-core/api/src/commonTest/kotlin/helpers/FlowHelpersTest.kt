package com.brbx.mvicore.helpers

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

internal class FlowHelpersTest {

    @Test
    fun `selectFlow transforms state flow`() = runTest {
        data class State(val x: Int)
        val delegate = TestMviDelegate<State, Unit, Unit, Any>(State(x = 1), viewModelScope = this)
        
        delegate.selectFlow { it.x }.test {
            assertEquals(1, awaitItem())
            
            delegate.reduce { copy(x = 2) }
            assertEquals(2, awaitItem())
            
            delegate.reduce { copy(x = 2) } // Same value, should be distinct
            expectNoEvents()
            
            delegate.reduce { copy(x = 3) }
            assertEquals(3, awaitItem())
        }
    }

    @Test
    fun `collectTask collects values from flow`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Unit, Any>(Unit, viewModelScope = this)
        val flow = flowOf(1, 2, 3)
        val collected = mutableListOf<Int>()
        
        with(delegate) {
            val job = flow.collectFlow { value ->
                collected.add(value)
            }
            job.join()
        }
        
        assertEquals(listOf(1, 2, 3), collected)
    }

    @Test
    fun `collectTaskIf collects only if condition is true`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Unit, Any>(Unit, viewModelScope = this)
        val flow = flowOf(1)
        var collected = false
        
        with(delegate) {
            val job1 = flow.collectFlowIf(condition = false) { collected = true }
            assertEquals(null, job1)
            assertEquals(false, collected)
            
            val job2 = flow.collectFlowIf(condition = true) { collected = true }
            job2?.join()
            assertEquals(true, collected)
        }
    }

    @Test
    fun `bind updates state from flow`() = runTest {
        data class State(val x: Int)
        val delegate = TestMviDelegate<State, Unit, Unit, Any>(State(x = 0), viewModelScope = this)
        val flow = flowOf(1, 2, 3)
        
        with(delegate) {
            val job = flow bind { copy(x = it) }
            job.join()
        }
        
        assertEquals(3, delegate.currentState.x)
    }

    @Test
    fun `bindIf updates state only if condition is true`() = runTest {
        data class State(val x: Int)
        val delegate = TestMviDelegate<State, Unit, Unit, Any>(State(x = 0), viewModelScope = this)
        val flow = flowOf(1)
        
        with(delegate) {
            val job1 = flow.bindIf(condition = false) { copy(x = it) }
            assertEquals(null, job1)
            assertEquals(0, delegate.currentState.x)
            
            val job2 = flow.bindIf(condition = true) { copy(x = it) }
            job2?.join()
            assertEquals(1, delegate.currentState.x)
        }
    }

    @Test
    fun `collectFlowLatest collects latest values`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Unit, Any>(Unit, viewModelScope = this)
        val flow = flow {
            emit(1)
            delay(50.milliseconds)
            emit(2)
        }
        val collected = mutableListOf<Int>()
        
        with(delegate) {
            val job = flow.collectFlowLatest { value ->
                delay(100.milliseconds)
                collected.add(value)
            }
            delay(200.milliseconds)
            job.cancel()
        }
        
        // Value 1 is cancelled by value 2
        assertEquals(listOf(2), collected)
    }

    @Test
    fun `collectFlowLatestIf collects only if condition is true`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Unit, Any>(Unit, viewModelScope = this)
        val flow = flowOf(1)
        var collected = false
        
        with(delegate) {
            val job1 = flow.collectFlowLatestIf(condition = false) { collected = true }
            assertEquals(null, job1)
            assertEquals(false, collected)
            
            val job2 = flow.collectFlowLatestIf(condition = true) { collected = true }
            job2?.join()
            assertEquals(true, collected)
        }
    }

    @Test
    fun `bindLatest updates state with latest values`() = runTest {
        data class State(val x: Int)
        val delegate = TestMviDelegate<State, Unit, Unit, Any>(State(x = 0), viewModelScope = this)
        val flow = flow {
            emit(1)
            delay(50.milliseconds)
            emit(2)
        }
        
        with(delegate) {
            val job = flow.collectFlowLatest { value ->
                delay(100.milliseconds)
                reduce { copy(x = value) }
            }
            delay(200.milliseconds)
            job.cancel()
        }
        
        // Value 1 is cancelled by value 2
        assertEquals(2, delegate.currentState.x)
    }

    @Test
    fun `bindLatest updates state from flow`() = runTest {
        data class State(val x: Int)
        val delegate = TestMviDelegate<State, Unit, Unit, Any>(State(x = 0), viewModelScope = this)
        val flow = flowOf(1, 2, 3)
        
        with(delegate) {
            val job = flow bindLatest { copy(x = it) }
            job.join()
        }
        
        assertEquals(3, delegate.currentState.x)
    }

    @Test
    fun `bindLatestIf updates state only if condition is true`() = runTest {
        data class State(val x: Int)
        val delegate = TestMviDelegate<State, Unit, Unit, Any>(State(x = 0), viewModelScope = this)
        val flow = flowOf(1)
        
        with(delegate) {
            val job1 = flow.bindLatestIf(condition = false) { copy(x = it) }
            assertEquals(null, job1)
            assertEquals(0, delegate.currentState.x)
            
            val job2 = flow.bindLatestIf(condition = true) { copy(x = it) }
            job2?.join()
            assertEquals(1, delegate.currentState.x)
        }
    }
}
