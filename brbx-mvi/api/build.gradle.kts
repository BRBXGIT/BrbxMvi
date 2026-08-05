plugins {
    // Kotlin lib
    alias(libs.plugins.brbxmvi.kotlin.library)
    // Publish
    alias(libs.plugins.brbxmvi.publish)
}

dependencies {

    // ViewModel
    api(libs.androidx.lifecycle.viewmodel)

    // Testing
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.junit)
}