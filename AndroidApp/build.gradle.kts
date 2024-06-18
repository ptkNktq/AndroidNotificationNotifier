plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.com.jaredsburrows.license) apply false
}

// https://github.com/facebook/react-native/issues/44501#issuecomment-2112926438
gradle.startParameter.excludedTaskNames.addAll(
    gradle.startParameter.taskNames.filter { it.contains("testClasses") }
)