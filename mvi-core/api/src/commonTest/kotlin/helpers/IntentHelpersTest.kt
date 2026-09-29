package com.brbx.mvicore.helpers

import com.brbx.mvi_core.helpers.dispatchIntent
import com.brbx.mvi_core.helpers.dispatchIntentIf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class IntentHelpersTest {

    internal sealed interface TestIntent {
        data object Intent1 : TestIntent
        data object Intent2 : TestIntent
    }

    @Test
    fun `dispatchIntent sends intent to scope`() {
        val delegate = TestMviDelegate<Unit, Unit, Unit, TestIntent>(Unit)
        delegate.dispatchIntent(TestIntent.Intent1)
        
        assertEquals(1, delegate.mviScope.dispatchedIntents.size)
        assertEquals(TestIntent.Intent1, delegate.mviScope.dispatchedIntents.first())
    }

    @Test
    fun `dispatchIntent can dispatch external intent type from specialized delegate`() {
        val delegate = TestMviDelegate<Unit, Unit, Unit, TestIntent.Intent1>(Unit)
        delegate.dispatchIntent(TestIntent.Intent2)
        
        assertEquals(1, delegate.mviScope.dispatchedIntents.size)
        assertEquals(TestIntent.Intent2, delegate.mviScope.dispatchedIntents.first())
    }

    @Test
    fun `dispatchIntentIf sends intent only when condition is true`() {
        val delegate = TestMviDelegate<Unit, Unit, Unit, TestIntent>(Unit)
        
        delegate.dispatchIntentIf(TestIntent.Intent1, condition = false)
        assertTrue(delegate.mviScope.dispatchedIntents.isEmpty())
        
        delegate.dispatchIntentIf(TestIntent.Intent2, condition = true)
        assertEquals(1, delegate.mviScope.dispatchedIntents.size)
        assertEquals(TestIntent.Intent2, delegate.mviScope.dispatchedIntents.first())
    }
}
