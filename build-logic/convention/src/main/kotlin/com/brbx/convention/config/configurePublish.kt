package com.brbx.convention.config

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

internal fun Project.configurePublish() {
    pluginManager.apply("maven-publish")

    group = "com.github.BRBXGIT.BrbxMvi"
    version = System.getenv("JITPACK_VERSION") ?: "1.0.0-LOCAL"

    // Fix problem with .jar/.kt files on JitPack DO NOT DELETE
    tasks.withType(
        org.gradle.api.publish.tasks.GenerateModuleMetadata::class.java
    ).configureEach {
        enabled = false
    }

    extensions.configure<PublishingExtension> {
        pluginManager.withPlugin("com.android.library") {
            afterEvaluate {
                publications.create<MavenPublication>("release") {
                    from(components["release"])
                    artifactId = project.path.replace(":", "-").removePrefix("-")
                }
            }
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            publications.create<MavenPublication>("java") {
                from(components["java"])
                artifactId = project.path.replace(":", "-").removePrefix("-")
            }
        }
    }
}