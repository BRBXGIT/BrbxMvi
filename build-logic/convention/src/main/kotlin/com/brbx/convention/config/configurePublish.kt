package com.brbx.convention.config

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.tasks.GenerateModuleMetadata
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

internal fun Project.configurePublish() {
    pluginManager.apply("maven-publish")

    group = "com.github.BRBXGIT.BrbxMvi"
    version = System.getenv("JITPACK_VERSION") ?: "1.1.0"

    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = true
    }

    val javadocJar = tasks.register<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        // Use dokkaHtml if dokka is applied
        if (pluginManager.hasPlugin("org.jetbrains.dokka")) {
            from(tasks.named("dokkaHtml"))
        }
    }

    extensions.configure<PublishingExtension> {
        publications.withType<MavenPublication>().configureEach {
            artifact(javadocJar)
        }
    }
}
