plugins {
    // Application
    alias(libs.plugins.android.application) apply false
    // Compose
    alias(libs.plugins.kotlin.compose) apply false
    // Android library
    alias(libs.plugins.android.library) apply false
    // Kotlin jvm (used in kotlin library convention plugin)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}