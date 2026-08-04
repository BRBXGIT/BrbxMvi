package com.brbx.api.helpers.view_model.vm

internal sealed interface TestEffect {
    data object Effect1 : TestEffect
    data object Effect2 : TestEffect
}