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
    compileOnly(libs.dokka.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = libs.plugins.brbxmvi.android.app.get().pluginId
            implementationClass = "com.brbx.convention.AndroidAppConventionPlugin"
        }

        register("kmpLibrary") {
            id = libs.plugins.brbxmvi.kmp.library.get().pluginId
            implementationClass = "com.brbx.convention.KmpLibraryConventionPlugin"
        }

        register("composeMultiplatform") {
            id = libs.plugins.brbxmvi.compose.multiplatform.get().pluginId
            implementationClass = "com.brbx.convention.ComposeMultiplatformConventionPlugin"
        }

        register("publish") {
            id = libs.plugins.brbxmvi.publish.get().pluginId
            implementationClass = "com.brbx.convention.PublishConventionPlugin"
        }
    }
}