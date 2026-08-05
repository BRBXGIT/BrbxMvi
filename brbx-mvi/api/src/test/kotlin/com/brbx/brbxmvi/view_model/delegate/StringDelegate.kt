package com.brbx.brbxmvi.view_model.delegate

import com.brbx.brbxmvi.contracts.MviScope
import com.brbx.brbxmvi.helpers.launchAction
import com.brbx.brbxmvi.helpers.reduce
import com.brbx.brbxmvi.view_model.vm.TestEffect
import com.brbx.brbxmvi.view_model.vm.TestIntent
import com.brbx.brbxmvi.view_model.vm.TestState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

internal interface StringDelegate : TestDelegate<TestIntent.StringIntent>

internal class StringDelegateImpl(
    override val scope: MviScope<TestState, TestEffect, TestIntent>,
    private val dispatcher: CoroutineDispatcher,
) : StringDelegate {

    override fun process(intent: TestIntent.StringIntent) {
        when (intent) {
            TestIntent.StringIntent.SuspendAddMvi -> addMvi()
            TestIntent.StringIntent.SuspendRemoveMvi -> removeMvi()
        }
    }

    private fun addMvi() {
        launchAction(context = dispatcher) {
            delay(duration = 2_000.milliseconds)
            reduce { copy(string = string + "Mvi") }
        }
    }

    private fun removeMvi() {
        launchAction(context = dispatcher) {
            delay(duration = 2_000.milliseconds)
            reduce { copy(string = string.removeSuffix("Mvi")) }
        }
    }
}