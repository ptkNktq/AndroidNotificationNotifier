plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.com.jaredsburrows.license) apply false
}

tasks.register("clean") {
    doFirst {
        delete(rootProject.buildDir)
    }
}