import com.android.build.api.dsl.LibraryExtension
import me.nya_n.notificationnotifier.configureLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

// used in build.gradle.kts > gradlePlugin
@Suppress("unused")
class CommonLibrary : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                configureLibrary(this)
            }
        }
    }
}