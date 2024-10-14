plugins {
    id("common.android")
}

android {
    namespace = "me.nya_n.notificationnotifier"

    defaultConfig {
        applicationId = "me.nya_n.notificationnotifier"
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":ui"))
    // diのために必要
    implementation(project(":domain"))
    implementation(project(":data:repository"))
    implementation(project(":model"))

    // その他
    implementation(libs.io.insert.koin)
    debugImplementation(libs.com.squareup.leakcanary.android)
}