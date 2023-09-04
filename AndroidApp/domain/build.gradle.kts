plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "me.nya_n.notificationnotifier.domain"
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
}

dependencies {
    implementation(libs.org.jetbrains.kotlin.stdlib)
    implementation(project(":data:repository"))
    api(project(":model"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.security.crypto)
    // compose for @Stable annotation
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)

    // test
    implementation(libs.junit)
    implementation(libs.com.google.truth)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.espresso.core)

    // その他
    implementation(libs.com.google.code.gson)
}