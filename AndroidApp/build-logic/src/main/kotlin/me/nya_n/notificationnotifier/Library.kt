package me.nya_n.notificationnotifier

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal fun Project.configureLibrary(
    extension: LibraryExtension
) {
    configureCommon(extension)
    extension.apply {
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}