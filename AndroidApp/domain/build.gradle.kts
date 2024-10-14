plugins {
    id("common.library")
}

android {
    namespace = "me.nya_n.notificationnotifier.domain"

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":data:repository"))
    api(project(":model"))

    // androidx
    implementation(libs.androidx.core.ktx) // text.isDigitsOnlyのためだけっぽい。必要？
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