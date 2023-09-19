plugins {
    id("common.library")
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.nya_n.notificationnotifier.data.repository"
}

dependencies {
    api(project(":model"))

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    // room
    api(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}