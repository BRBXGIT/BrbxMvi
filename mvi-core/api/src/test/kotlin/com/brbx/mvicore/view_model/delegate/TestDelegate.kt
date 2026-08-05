package com.brbx.mvicore.view_model.delegate

import com.brbx.mvicore.contracts.MviDelegate
import com.brbx.mvicore.view_model.vm.TestEffect
import com.brbx.mvicore.view_model.vm.TestIntent
import com.brbx.mvicore.view_model.vm.TestState

internal interface TestDelegate<in Intent : TestIntent> : MviDelegate<TestState, TestEffect, Intent>