package com.brbx.mvicore.view_model

import app.cash.turbine.test
import com.brbx.mvicore.view_model.delegate.TestDelegateFactoryImpl
import com.brbx.mvicore.view_model.vm.TestIntent
import com.brbx.mvicore.view_model.vm.TestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ContainedMviViewModelTest {

    private val factory = TestDelegateFactoryImpl()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TestViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TestViewModel(delegateFactory = factory, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `delegate methods are called correctly`() = runTest {
        val state = viewModel.state
        state.test {
            assertEquals(0, awaitItem().int)

            viewModel.dispatchIntent(TestIntent.IntIntent.PlusOne)
            runCurrent()
            assertEquals(1, awaitItem().int)

            viewModel.dispatchIntent(TestIntent.IntIntent.MinusOne)
            runCurrent()
            assertEquals(0, awaitItem().int)
        }
    }

    @Test
    fun `delegate suspend methods are called correctly`() = runTest {
        val state = viewModel.state
        state.test {
            assertEquals("", awaitItem().string)

            viewModel.dispatchIntent(TestIntent.StringIntent.SuspendAddMvi)
            advanceUntilIdle()
            assertEquals("Mvi", awaitItem().string)

            viewModel.dispatchIntent(TestIntent.StringIntent.SuspendRemoveMvi)
            advanceUntilIdle()
            assertEquals("", awaitItem().string)
        }
    }
}