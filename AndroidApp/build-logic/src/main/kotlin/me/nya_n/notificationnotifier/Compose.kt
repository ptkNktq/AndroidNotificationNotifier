package me.nya_n.notificationnotifier

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureCompose(
    extension: CommonExtension<*, *, *, *, *, *>
) {
    extension.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.version("androidx-compose-compiler")
        }
    }
}