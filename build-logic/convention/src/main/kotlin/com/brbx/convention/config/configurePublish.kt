package com.brbx.convention.config

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType

import org.gradle.api.publish.tasks.GenerateModuleMetadata

internal fun Project.configurePublish() {
    pluginManager.apply("maven-publish")

    group = "com.github.BRBXGIT.BrbxMvi"
    version = System.getenv("JITPACK_VERSION") ?: "1.1.0"

    val baseArtifactId = path
        .replace(oldValue = ":", newValue = "-").removePrefix("-").ifEmpty { name }

    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }

    extensions.configure<PublishingExtension> {
        // --- Android Library ---
        pluginManager.withPlugin("com.android.library") {
            extensions.configure<LibraryExtension> {
                publishing {
                    singleVariant("release") {
                        withSourcesJar()
                        withJavadocJar()
                    }
                }
            }
            afterEvaluate {
                publications.create<MavenPublication>("release") {
                    from(components["release"])
                    artifactId = baseArtifactId
                }
            }
        }

        // --- Kotlin JVM ---
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            extensions.configure<JavaPluginExtension> {
                withSourcesJar()
                withJavadocJar()
            }
            afterEvaluate {
                publications.create<MavenPublication>("java") {
                    from(components["java"])
                    artifactId = baseArtifactId
                }
            }
        }

        // --- Kotlin Multiplatform (KMP) ---
        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            tasks.withType<GenerateModuleMetadata>().configureEach {
                enabled = true
            }
            afterEvaluate {
                publications.withType<MavenPublication>().configureEach {
                    if (artifactId.startsWith(prefix = name)) {
                        artifactId = artifactId.replaceFirst(oldValue = name, newValue = baseArtifactId)
                    }
                }
            }
        }
    }
}