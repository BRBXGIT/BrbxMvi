package com.brbx.convention

import com.brbx.convention.config.configurePublish
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class PublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configurePublish()
    }
}