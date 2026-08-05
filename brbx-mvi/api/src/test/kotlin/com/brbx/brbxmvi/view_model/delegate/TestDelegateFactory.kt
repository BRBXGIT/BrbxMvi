package com.brbx.brbxmvi.view_model.delegate

import com.brbx.brbxmvi.contracts.MviScope
import com.brbx.brbxmvi.view_model.vm.TestEffect
import com.brbx.brbxmvi.view_model.vm.TestIntent
import com.brbx.brbxmvi.view_model.vm.TestState
import kotlinx.coroutines.CoroutineDispatcher

private typealias DelegateMviScope = MviScope<TestState, TestEffect, TestIntent>

internal interface TestDelegateFactory {

    fun createIntDelegate(mviScope: DelegateMviScope): IntDelegate

    fun createStringDelegate(
        mviScope: DelegateMviScope,
        dispatcher: CoroutineDispatcher,
    ): StringDelegate
}

internal class TestDelegateFactoryImpl : TestDelegateFactory {

    override fun createIntDelegate(mviScope: DelegateMviScope): IntDelegate =
        IntDelegateImpl(mviScope)

    override fun createStringDelegate(
        mviScope: DelegateMviScope,
        dispatcher: CoroutineDispatcher,
    ): StringDelegate = StringDelegateImpl(mviScope, dispatcher)
}