plugins {
    id("common.library.compose")
    alias(libs.plugins.com.jaredsburrows.license)
}

android {
    namespace = "me.nya_n.notificationnotifier.ui"
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
    implementation(libs.bundles.androidx.compose)

    // その他
    implementation(libs.com.google.android.material)
    implementation(libs.io.insert.koin)
    implementation(libs.io.insert.koin.compose)
}