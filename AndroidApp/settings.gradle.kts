pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
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