import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.example.build_logic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.libertyflow.android.application.get().pluginId
            implementationClass = "com.brbx.convention.AndroidApplicationConventionPlugin"
        }

        register("kotlinLibrary") {
            id = libs.plugins.libertyflow.kotlin.library.get().pluginId
            implementationClass = "com.brbx.convention.KotlinLibraryConventionPlugin"
        }

        register("androidLibrary") {
            id = libs.plugins.libertyflow.android.library.get().pluginId
            implementationClass = "com.brbx.convention.AndroidLibraryConventionPlugin"
        }
    }
}