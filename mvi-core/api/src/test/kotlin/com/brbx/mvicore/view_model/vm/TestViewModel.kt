package com.brbx.mvicore.view_model.vm

import com.brbx.mvicore.base.ContainedMviViewModel
import com.brbx.mvicore.view_model.delegate.TestDelegateFactory
import kotlinx.coroutines.CoroutineDispatcher

internal class TestViewModel(
    delegateFactory: TestDelegateFactory,
    dispatcher: CoroutineDispatcher,
) : ContainedMviViewModel<TestState, TestEffect, TestScreenEffect, TestIntent>(
    initialState = TestState(),
) {
    private val intDelegate = delegateFactory.createIntDelegate(mviScope = scope)
    private val stringDelegate = delegateFactory.createStringDelegate(mviScope = scope, dispatcher)

    override fun dispatchIntent(intent: TestIntent) {
        when (intent) {
            is TestIntent.IntIntent-> intDelegate(intent)
            is TestIntent.StringIntent -> stringDelegate(intent)
        }
    }
}