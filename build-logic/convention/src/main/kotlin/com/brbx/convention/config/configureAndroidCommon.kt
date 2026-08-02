package com.brbx.convention.config

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroidCommon(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        val modulePath = project.path
            .split(":")
            .filter { it.isNotEmpty() }
            .joinToString(separator = ".") { it.replace("-", "_") }

        namespace = if (modulePath.isNotEmpty()) {
            "com.brbx.$modulePath"
        } else "com.brbx"

        compileSdk = 37

        defaultConfig.minSdk = 28

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}