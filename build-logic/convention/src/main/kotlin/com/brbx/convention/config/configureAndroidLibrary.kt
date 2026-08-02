package com.brbx.convention.config

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureAndroidLibrary(
    block: LibraryExtension.() -> Unit = {}
) {
    pluginManager.apply("com.android.library")
    
    extensions.configure<LibraryExtension> {
        configureAndroidCommon(this)
        
        block()
    }
}