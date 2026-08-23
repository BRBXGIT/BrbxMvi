plugins {
    // Android Application
    alias(libs.plugins.brbxmvi.android.app)
}

dependencies {

    implementation("com.github.BRBXGIT:BrbxMvi:1.1.2")
    // Core
    implementation(libs.androidx.core.ktx)
}