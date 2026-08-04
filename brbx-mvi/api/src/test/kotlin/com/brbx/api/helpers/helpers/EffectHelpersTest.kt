package com.brbx.api.helpers.helpers

import com.brbx.api.helpers.postEffect
import com.brbx.api.helpers.postEffectIf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class EffectHelpersTest {

    internal sealed interface TestEffect {
        data object Effect1 : TestEffect
        data object Effect2 : TestEffect
    }

    @Test
    fun `postEffect dispatches effect to scope`() {
        val delegate = TestMviDelegate<Unit, TestEffect, Any>(Unit)
        delegate.postEffect(TestEffect.Effect1)
        
        assertEquals(1, delegate.scope.postedEffects.size)
        assertEquals(TestEffect.Effect1, delegate.scope.postedEffects.first())
    }

    @Test
    fun `postEffectIf dispatches effect only when condition is true`() {
        val delegate = TestMviDelegate<Unit, TestEffect, Any>(Unit)
        
        delegate.postEffectIf(TestEffect.Effect1, condition = false)
        assertTrue(delegate.scope.postedEffects.isEmpty())
        
        delegate.postEffectIf(TestEffect.Effect2, condition = true)
        assertEquals(1, delegate.scope.postedEffects.size)
        assertEquals(TestEffect.Effect2, delegate.scope.postedEffects.first())
    }
}
