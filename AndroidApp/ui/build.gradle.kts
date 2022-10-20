plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.cookpad.android.plugin.license-tools")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 30
        targetSdk = 32

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation(project(":domain"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // nav
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // その他
    implementation(libs.com.google.android.material)
    implementation(libs.io.insert.koin.android)
}