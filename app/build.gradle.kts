plugins {
    // Android Application
    alias(libs.plugins.brbxmvi.android.application)
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.core)
}