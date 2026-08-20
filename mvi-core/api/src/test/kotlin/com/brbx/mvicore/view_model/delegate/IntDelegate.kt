package com.brbx.mvicore.view_model.delegate

import com.brbx.mvicore.contracts.MviScope
import com.brbx.mvicore.helpers.reduce
import com.brbx.mvicore.view_model.vm.TestEffect
import com.brbx.mvicore.view_model.vm.TestIntent
import com.brbx.mvicore.view_model.vm.TestScreenEffect
import com.brbx.mvicore.view_model.vm.TestState

internal interface IntDelegate : TestDelegate<TestIntent.IntIntent>

internal class IntDelegateImpl(
    override val scope: MviScope<TestState, TestEffect, TestScreenEffect, TestIntent>,
) : IntDelegate {

    override fun invoke(intent: TestIntent.IntIntent) {
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