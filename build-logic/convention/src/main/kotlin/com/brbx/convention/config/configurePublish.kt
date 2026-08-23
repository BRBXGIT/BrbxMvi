package com.brbx.convention.config

import org.gradle.api.Project
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.kotlin.dsl.withType

internal fun Project.configurePublish() {
    pluginManager.apply("maven-publish")

    group = "com.github.BRBXGIT.BrbxMvi"
    version = System.getenv("JITPACK_VERSION") ?: "1.1.0"

    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = true
    }
}