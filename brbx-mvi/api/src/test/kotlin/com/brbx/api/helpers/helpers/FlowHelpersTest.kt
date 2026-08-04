package com.brbx.api.helpers.helpers

import app.cash.turbine.test
import com.brbx.api.helpers.collectTask
import com.brbx.api.helpers.collectTaskIf
import com.brbx.api.helpers.reduce
import com.brbx.api.helpers.selectFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FlowHelpersTest {

    @Test
    fun `selectFlow transforms state flow`() = runTest {
        data class State(val x: Int)
        val delegate = TestMviDelegate<State, Unit, Any>(State(x = 1))
        
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
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        val flow = flowOf(1, 2, 3)
        val collected = mutableListOf<Int>()
        
        with(delegate) {
            val job = flow.collectTask { value ->
                collected.add(value)
            }
            job.join()
        }
        
        assertEquals(listOf(1, 2, 3), collected)
    }

    @Test
    fun `collectTaskIf collects only if condition is true`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        val flow = flowOf(1)
        var collected = false
        
        with(delegate) {
            val job1 = flow.collectTaskIf(condition = false) { collected = true }
            assertEquals(null, job1)
            assertEquals(false, collected)
            
            val job2 = flow.collectTaskIf(condition = true) { collected = true }
            job2?.join()
            assertEquals(true, collected)
        }
    }
}
