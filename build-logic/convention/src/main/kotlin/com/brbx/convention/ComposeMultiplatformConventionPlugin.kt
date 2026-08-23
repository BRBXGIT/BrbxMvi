package com.brbx.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(receiver = pluginManager) {
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }
    }
}