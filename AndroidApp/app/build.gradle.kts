plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.cookpad.android.plugin.license-tools")
}

android {
    namespace = "me.nya_n.notificationnotifier"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "me.nya_n.notificationnotifier"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = Versions.JAVA_VERSION
        targetCompatibility = Versions.JAVA_VERSION
    }

    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }

    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}

dependencies {
    implementation(project(":ui"))
    // diのために必要
    implementation(project(":domain"))
    implementation(project(":data:repository"))

    // その他
    implementation(libs.io.insert.koin)
    implementation(libs.com.squareup.leakcanary.android)
}