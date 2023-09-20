import com.android.build.api.dsl.ApplicationExtension
import me.nya_n.notificationnotifier.configureApplication
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

// used in build.gradle.kts > gradlePlugin
@Suppress("unused")
class CommonApplication : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<ApplicationExtension> {
                configureApplication(this)
            }
        }
    }
}