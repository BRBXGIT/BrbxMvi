package com.brbx.brbxmvi.view_model.vm

import com.brbx.brbxmvi.base.ContainedMviViewModel
import com.brbx.brbxmvi.view_model.delegate.TestDelegateFactory
import kotlinx.coroutines.CoroutineDispatcher

internal class TestViewModel(
    delegateFactory: TestDelegateFactory,
    dispatcher: CoroutineDispatcher,
) : ContainedMviViewModel<TestState, TestEffect, TestIntent>(
    initialState = TestState(),
) {
    private val intDelegate = delegateFactory.createIntDelegate(mviScope = scope)
    private val stringDelegate = delegateFactory.createStringDelegate(mviScope = scope, dispatcher)

    override fun dispatchIntent(intent: TestIntent) {
        when (intent) {
            is TestIntent.IntIntent-> intDelegate.process(intent)
            is TestIntent.StringIntent -> stringDelegate.process(intent)
        }
    }
}