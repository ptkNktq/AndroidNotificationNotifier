plugins {
    id("common.library")
    alias(libs.plugins.com.jaredsburrows.license)
}

android {
    namespace = "me.nya_n.notificationnotifier.ui"

    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.KOTLIN_COMPILER_EXTENSION_VERSION
    }
}

dependencies {
    implementation(project(":domain"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
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