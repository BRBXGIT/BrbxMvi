package com.brbx.mvicore.view_model.vm

internal sealed interface TestScreenEffect {
    data object ScreenEffect1 : TestScreenEffect
    data object ScreenEffect2 : TestScreenEffect
}