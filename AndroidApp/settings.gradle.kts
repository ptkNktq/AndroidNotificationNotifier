pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NotificationNotifier"
include(
    ":app",
    ":ui",
    ":domain",
    ":data:repository",
    ":model",
)