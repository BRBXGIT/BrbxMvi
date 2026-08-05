package com.brbx.brbxmvi.view_model.delegate

import com.brbx.brbxmvi.contracts.MviDelegate
import com.brbx.brbxmvi.view_model.vm.TestEffect
import com.brbx.brbxmvi.view_model.vm.TestIntent
import com.brbx.brbxmvi.view_model.vm.TestState

internal interface TestDelegate<in Intent : TestIntent> : MviDelegate<TestState, TestEffect, Intent>