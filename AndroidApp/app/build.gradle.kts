plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // nav
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // test
    implementation(libs.junit)
    implementation(libs.com.google.truth)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.espresso.core)

    // その他
    implementation(libs.com.google.android.material)
    implementation(libs.io.insert.koin.android)
    implementation(libs.com.google.code.gson)
    implementation(libs.com.squareup.leakcanary.android)
}