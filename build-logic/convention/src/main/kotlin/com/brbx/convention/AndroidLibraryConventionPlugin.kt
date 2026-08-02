package com.brbx.convention

import com.brbx.convention.config.configureAndroidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configureAndroidLibrary()
    }
}