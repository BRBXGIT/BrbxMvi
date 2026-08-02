package com.brbx.convention

import com.android.build.api.dsl.ApplicationExtension
import com.brbx.convention.config.configureAndroidCommon
import com.brbx.convention.config.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(receiver = target) {
            pluginManager.apply("com.android.application")

            extensions.configure<ApplicationExtension> {
                configureAndroidCommon(this)

                defaultConfig {
                    applicationId = "com.brbx.libertyflow"
                    targetSdk = 37
                    versionCode = 1
                    versionName = "1.0"
                }

                configureCompose(this)

                buildTypes {
                    release {
                        optimization {
                            enable = false
                        }
                    }
                }
            }
        }
    }
}