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
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.com.google.truth)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // その他
    implementation(libs.com.google.code.gson)
}