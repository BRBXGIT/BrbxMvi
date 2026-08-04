package com.brbx.api.helpers.view_model.vm

import com.brbx.api.base.ContainedMviViewModel

class TestViewModel : ContainedMviViewModel<TestState, TestEffect, TestIntent>(
    initialState = TestState(),
) {

}