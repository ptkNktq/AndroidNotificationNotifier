plugins {
    id("common.library.compose")
    alias(libs.plugins.com.jaredsburrows.license)
    alias(libs.plugins.screenshot)
}

android {
    namespace = "me.nya_n.notificationnotifier.ui"
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(project(":domain"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.splashscreen)
    // compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    debugImplementation(libs.androidx.compose.ui.tooling)
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.bundles.androidx.compose)

    // その他
    implementation(libs.io.insert.koin)
    implementation(libs.io.insert.koin.compose)
}