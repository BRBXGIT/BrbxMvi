package com.brbx.api.helpers.view_model.delegate

import com.brbx.api.contracts.MviScope
import com.brbx.api.helpers.reduce
import com.brbx.api.helpers.view_model.vm.TestEffect
import com.brbx.api.helpers.view_model.vm.TestIntent
import com.brbx.api.helpers.view_model.vm.TestState

interface IntDelegate : TestDelegate<TestIntent.IntIntent>

class IntDelegateImpl(
    override val scope: MviScope<TestState, TestEffect, TestIntent.IntIntent>,
) : IntDelegate {

    override fun process(intent: TestIntent.IntIntent) {
        when (intent) {
            TestIntent.IntIntent.MinusOne -> minusOne()
            TestIntent.IntIntent.PlusOne -> plusOne()
        }
    }

    private fun minusOne() {
        reduce { copy(int = int - 1) }
    }

    private fun plusOne() {
        reduce { copy(int = int + 1) }
    }
}