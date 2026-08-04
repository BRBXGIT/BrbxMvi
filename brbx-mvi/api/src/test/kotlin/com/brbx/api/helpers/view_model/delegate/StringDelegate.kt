package com.brbx.api.helpers.view_model.delegate

import com.brbx.api.contracts.MviScope
import com.brbx.api.helpers.launchTask
import com.brbx.api.helpers.reduce
import com.brbx.api.helpers.view_model.vm.TestEffect
import com.brbx.api.helpers.view_model.vm.TestIntent
import com.brbx.api.helpers.view_model.vm.TestState
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
        launchTask(context = dispatcher) {
            delay(duration = 2_000.milliseconds)
            reduce { copy(string = string + "Mvi") }
        }
    }

    private fun removeMvi() {
        launchTask(context = dispatcher) {
            delay(duration = 2_000.milliseconds)
            reduce { copy(string = string.removeSuffix("Mvi")) }
        }
    }
}