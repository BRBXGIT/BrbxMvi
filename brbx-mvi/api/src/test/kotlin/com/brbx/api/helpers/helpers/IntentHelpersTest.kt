package com.brbx.api.helpers.helpers

import com.brbx.api.helpers.dispatchIntent
import com.brbx.api.helpers.dispatchIntentIf
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
        val delegate = TestMviDelegate<Unit, Unit, TestIntent>(Unit)
        delegate.dispatchIntent(TestIntent.Intent1)
        
        assertEquals(1, delegate.scope.dispatchedIntents.size)
        assertEquals(TestIntent.Intent1, delegate.scope.dispatchedIntents.first())
    }

    @Test
    fun `dispatchIntentIf sends intent only when condition is true`() {
        val delegate = TestMviDelegate<Unit, Unit, TestIntent>(Unit)
        
        delegate.dispatchIntentIf(TestIntent.Intent1, condition = false)
        assertTrue(delegate.scope.dispatchedIntents.isEmpty())
        
        delegate.dispatchIntentIf(TestIntent.Intent2, condition = true)
        assertEquals(1, delegate.scope.dispatchedIntents.size)
        assertEquals(TestIntent.Intent2, delegate.scope.dispatchedIntents.first())
    }
}
