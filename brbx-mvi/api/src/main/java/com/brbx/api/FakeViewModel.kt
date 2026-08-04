package com.brbx.api

import com.brbx.api.base.ContainedMviViewModel
import com.brbx.api.base.MviViewModel
import com.brbx.api.contracts.MviContainer
import com.brbx.api.contracts.MviDelegate
import com.brbx.api.contracts.MviScope
import com.brbx.api.contracts.mviScope
import com.brbx.api.helpers.asyncTask
import com.brbx.api.helpers.launchTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeDelegate(
    override val scope: MviScope<Unit, Unit, Unit>
) : MviDelegate<Unit, Unit, Unit> {

    override fun process(intent: Unit) {

    }
}

data class SomeState(
    val x: Int = 1
)

sealed interface SomeIntent {
    sealed interface Parent : SomeIntent {
        data object ChildIntent : Parent
    }

    sealed interface Parent2 : SomeIntent {
        data object ChildIntent2 : Parent2
    }
}

class FakeViewModel : ContainedMviViewModel<SomeState, Unit, SomeIntent>(
    initialState = SomeState(),
) {

}