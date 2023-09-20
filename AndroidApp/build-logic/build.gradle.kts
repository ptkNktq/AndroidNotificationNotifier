plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("common.application") {
            id = "common.android"
            implementationClass = "CommonApplication"
        }
        register("common.library") {
            id = "common.library"
            implementationClass = "CommonLibrary"
        }
        register("common.library.compose") {
            id = "common.library.compose"
            implementationClass = "CommonLibraryCompose"
        }
    }
}