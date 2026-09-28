package com.brbx.mvicore.view_model.delegate

import com.brbx.mvi_core.contracts.IntentDelegate
import com.brbx.mvi_core.contracts.MviDelegate
import com.brbx.mvicore.view_model.vm.TestEffect
import com.brbx.mvicore.view_model.vm.TestIntent
import com.brbx.mvicore.view_model.vm.TestScreenEffect
import com.brbx.mvicore.view_model.vm.TestState

internal interface TestDelegate<in Intent : TestIntent> :
    IntentDelegate<TestState, TestEffect, TestScreenEffect, Intent>