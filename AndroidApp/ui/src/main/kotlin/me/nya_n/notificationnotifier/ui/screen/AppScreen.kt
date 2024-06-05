package me.nya_n.notificationnotifier.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.screen.detail.DetailScreen
import me.nya_n.notificationnotifier.ui.screen.license.LicenseScreen
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import java.net.URLEncoder

@Composable
fun AppScreen() {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // TODO:
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                DetailScreen(
                    navController = navController,
                    app = app
                )
            }
        }
    }
}

/** 画面遷移情報 */
sealed class Screen(
    val name: String,
    private val args: List<String> = emptyList()
) {
    data object Main : Screen("main")
    data object License : Screen("license")
    data object Detail : Screen("detail", listOf("app"))

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
