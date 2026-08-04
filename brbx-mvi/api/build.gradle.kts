plugins {
    alias(libs.plugins.brbxmvi.kotlin.library)
}

tasks.test {
    useJUnit()
}

dependencies {

    // ViewModel
    api(libs.androidx.lifecycle.viewmodel)

    // Testing
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.junit)
}