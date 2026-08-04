package com.brbx.api.helpers.view_model.vm

internal sealed interface TestIntent {
    sealed interface IntIntent : TestIntent {
        data object PlusOne : IntIntent
        data object MinusOne : IntIntent
    }

    sealed interface StringIntent : TestIntent {
        data object SuspendAddMvi : StringIntent
        data object SuspendRemoveMvi : StringIntent
    }
}