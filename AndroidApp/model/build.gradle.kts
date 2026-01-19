plugins {
    id("common.library")
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.nya_n.notificationnotifier.model"
}

dependencies {
    // androidx
    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // その他
    api(libs.com.google.code.gson)
}
