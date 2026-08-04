package com.brbx.api.helpers.view_model.delegate

import com.brbx.api.contracts.MviDelegate
import com.brbx.api.helpers.view_model.vm.TestEffect
import com.brbx.api.helpers.view_model.vm.TestIntent
import com.brbx.api.helpers.view_model.vm.TestState

internal interface TestDelegate<in Intent : TestIntent> : MviDelegate<TestState, TestEffect, Intent>