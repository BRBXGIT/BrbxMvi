package com.brbx.api

import com.brbx.api.contracts.BrbxMviContainer
import com.brbx.api.contracts.BrbxMviDelegate
import com.brbx.api.contracts.MviScope
import com.brbx.api.contracts.mviScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeDelegate(
    override val scope: MviScope<Unit, Unit, Unit>
) : BrbxMviDelegate<Unit, Unit, Unit> {

    override fun process(intent: Unit) {

    }
}

class FakeViewModel : BrbxMviContainer<Unit, Unit, Unit> {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(Unit)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<Unit>()
    val effects = _effects.asSharedFlow()
}