package me.nya_n.notificationnotifier.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.screen.detail.DetailScreen
import me.nya_n.notificationnotifier.ui.screen.license.LicenseScreen
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import java.net.URLEncoder

@Composable
@ExperimentalPagerApi
fun AppScreen() {
    val navController = rememberNavController()
    AppTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.Main.name
        ) {
            composable(Screen.Main.route) { MainScreen(navController) }
            composable(Screen.License.route) { LicenseScreen() }
            composable(Screen.Detail.route) {
                val app = Gson().fromJson(
                    it.arguments?.getString("app"),
                    InstalledApp::class.java
                )
                DetailScreen(app)
            }
        }
    }
}

/**
 * 画面遷移情報
 */
sealed class Screen(
    val name: String,
    private val args: List<String> = emptyList()
) {
    object Main : Screen("main")
    object License : Screen("license")
    object Detail : Screen("detail", listOf("app"))

    val route: String
        get() {
            return if (args.isEmpty()) {
                name
            } else {
                "$name/${args.joinToString(separator = "/") { "{$it}" }}"
            }
        }

    fun createRouteWithParams(
        params: List<Any>
    ): String {
        return "$name/" + params.joinToString(separator = "/") {
            URLEncoder.encode(Gson().toJson(it), "UTF-8")
        }
    }
}
