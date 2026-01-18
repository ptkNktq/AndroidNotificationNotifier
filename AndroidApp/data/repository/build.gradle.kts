plugins {
    id("common.library")
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.nya_n.notificationnotifier.data.repository"
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    api(project(":model"))

    // androidx
    implementation(libs.androidx.core.ktx) // SharedPreferences#editの拡張関数用
    implementation(libs.androidx.security.crypto)
    // room
    api(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // test
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.com.google.truth)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}