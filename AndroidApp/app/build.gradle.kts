plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.cookpad.android.plugin.license-tools")
}

android {
    compileSdk = 32
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "me.nya_n.notificationnotifier"
        minSdk = 30
        targetSdk = 32
        versionCode = 3
        versionName = "4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}

dependencies {
    implementation(libs.org.jetbrains.kotlin.stdlib)
    implementation(project(":ui"))
    // diのために必要
    implementation(project(":domain"))
    implementation(project(":data:repository"))

    // その他
    implementation(libs.io.insert.koin.android)
    implementation(libs.com.squareup.leakcanary.android)
}