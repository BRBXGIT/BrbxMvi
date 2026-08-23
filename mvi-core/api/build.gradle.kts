plugins {
    // Kotlin lib
    alias(libs.plugins.brbxmvi.kmp.library)
    // Publish
    alias(libs.plugins.brbxmvi.publish)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // ViewModel
            api(libs.androidx.lifecycle.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.junit)
        }
    }
}