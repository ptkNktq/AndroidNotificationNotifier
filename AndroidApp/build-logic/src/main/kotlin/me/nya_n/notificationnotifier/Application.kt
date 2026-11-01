package me.nya_n.notificationnotifier

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal fun Project.configureApplication(
    extension: ApplicationExtension
) {
    configureCommon(extension)
    extension.apply {
        defaultConfig {
            targetSdk = libs.version("targetSdk").toInt()
            versionCode = libs.version("versionCode").toInt()
            versionName = libs.version("versionName")
        }
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