plugins {
    id("common.library.compose")
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.nya_n.notificationnotifier.model"
}

dependencies {
    // androidx
    implementation(libs.androidx.core.ktx)
    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // compose for @Stable annotation
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)

    // その他
    api(libs.com.google.code.gson)
    implementation(libs.com.google.android.material)
}

val Project.catalog
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")