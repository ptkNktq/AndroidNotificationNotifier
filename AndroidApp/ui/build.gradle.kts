plugins {
    id("com.android.library")
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.cookpad.android.plugin.license-tools")
}

android {
    namespace = "me.nya_n.notificationnotifier.ui"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.JAVA_VERSION
        targetCompatibility = Versions.JAVA_VERSION
    }
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