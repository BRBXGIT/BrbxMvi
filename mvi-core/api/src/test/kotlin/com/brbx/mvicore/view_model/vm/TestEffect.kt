package com.brbx.mvicore.view_model.vm

internal sealed interface TestEffect {
    data object Effect1 : TestEffect
    data object Effect2 : TestEffect
}