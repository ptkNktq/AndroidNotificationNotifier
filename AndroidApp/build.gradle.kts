// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // TODO: 乗り換えただけでまだこっちのOSSライセンス表示は対応していないので気が向いたら
        classpath(libs.oss.licenses.plugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
}

tasks.register("clean") {
    doFirst {
        delete(rootProject.buildDir)
    }
}