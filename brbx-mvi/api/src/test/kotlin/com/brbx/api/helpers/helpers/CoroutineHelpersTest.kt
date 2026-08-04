package com.brbx.api.helpers.helpers

import com.brbx.api.helpers.asyncTask
import com.brbx.api.helpers.asyncTaskIf
import com.brbx.api.helpers.launchTask
import com.brbx.api.helpers.launchTaskIf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineHelpersTest {

    @Test
    fun `launchTask executes block`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        var executed = false
        
        val job = delegate.launchTask {
            executed = true
        }
        job.join()
        
        assertEquals(true, executed)
    }

    @Test
    fun `launchTaskIf executes block only when condition is true`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        var executedCount = 0
        
        val job1 = delegate.launchTaskIf(condition = false) {
            executedCount++
        }
        assertNull(job1)
        
        val job2 = delegate.launchTaskIf(condition = true) {
            executedCount++
        }
        assertNotNull(job2)
        job2?.join()
        
        assertEquals(1, executedCount)
    }

    @Test
    fun `asyncTask returns result from block`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        
        val deferred = delegate.asyncTask {
            "result"
        }
        
        assertEquals("result", deferred.await())
    }

    @Test
    fun `asyncTaskIf returns deferred only when condition is true`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        
        val deferred1 = delegate.asyncTaskIf(condition = false) {
            "ignored"
        }
        assertNull(deferred1)
        
        val deferred2 = delegate.asyncTaskIf(condition = true) {
            "success"
        }
        assertNotNull(deferred2)
        assertEquals("success", deferred2?.await())
    }
}
