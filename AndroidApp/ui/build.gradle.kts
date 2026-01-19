plugins {
    id("common.library.compose")
    // note: 最新のgradle/agp非対応
//    alias(libs.plugins.com.jaredsburrows.license)
    alias(libs.plugins.screenshot)
}

android {
    namespace = "me.nya_n.notificationnotifier.ui"
    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
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
    screenshotTestImplementation(libs.screenshot.validation.api)
    implementation(libs.bundles.androidx.compose)

    // その他
    implementation(libs.io.insert.koin)
    implementation(libs.io.insert.koin.compose)
}
