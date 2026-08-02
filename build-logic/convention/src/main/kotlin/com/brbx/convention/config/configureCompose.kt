package com.brbx.convention.config

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureCompose(
    commonExtension: CommonExtension,
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    commonExtension.apply {
        buildFeatures.compose = true
    }
}