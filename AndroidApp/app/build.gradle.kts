plugins {
    id("common.android")
}

android {
    namespace = "me.nya_n.notificationnotifier"

    defaultConfig {
        applicationId = "me.nya_n.notificationnotifier"
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