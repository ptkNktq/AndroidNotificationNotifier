pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "NotificationNotifier"
include(
    ":app",
    ":ui",
    ":domain",
    ":data:repository",
    ":model",
)