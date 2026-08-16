plugins {
    // Android Application
    alias(libs.plugins.brbxmvi.android.application)
}

dependencies {

    implementation("com.github.BRBXGIT:BrbxMvi:1.1.2")
    // Core
    implementation(libs.androidx.core.ktx)
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.core)
}