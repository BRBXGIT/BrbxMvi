package com.brbx.api.helpers.helpers

import com.brbx.api.helpers.MviApiWithIf
import com.brbx.api.helpers.asyncAction
import com.brbx.api.helpers.asyncActionIf
import com.brbx.api.helpers.launchAction
import com.brbx.api.helpers.launchActionIf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(MviApiWithIf::class)
internal class CoroutineHelpersTest {

    @Test
    fun `launchTask executes block`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        var executed = false
        
        val job = delegate.launchAction {
            executed = true
        }
        job.join()
        
        assertEquals(true, executed)
    }

    @Test
    fun `launchTaskIf executes block only when condition is true`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        var executedCount = 0
        
        val job1 = delegate.launchActionIf(condition = false) {
            executedCount++
        }
        assertNull(job1)
        
        val job2 = delegate.launchActionIf(condition = true) {
            executedCount++
        }
        assertNotNull(job2)
        job2?.join()
        
        assertEquals(1, executedCount)
    }

    @Test
    fun `asyncTask returns result from block`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        
        val deferred = delegate.asyncAction {
            "result"
        }
        
        assertEquals("result", deferred.await())
    }

    @Test
    fun `asyncTaskIf returns deferred only when condition is true`() = runTest {
        val delegate = TestMviDelegate<Unit, Unit, Any>(Unit)
        
        val deferred1 = delegate.asyncActionIf(condition = false) {
            "ignored"
        }
        assertNull(deferred1)
        
        val deferred2 = delegate.asyncActionIf(condition = true) {
            "success"
        }
        assertNotNull(deferred2)
        assertEquals("success", deferred2?.await())
    }
}
