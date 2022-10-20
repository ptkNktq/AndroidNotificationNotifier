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
    ":domain",
    ":data:repository",
    ":model",
)