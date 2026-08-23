package com.brbx.convention.config

import com.brbx.convention.constants.Java
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal fun KotlinProjectExtension.configureJvmToolchain() {
    jvmToolchain(jdkVersion = Java.JdkV)
}