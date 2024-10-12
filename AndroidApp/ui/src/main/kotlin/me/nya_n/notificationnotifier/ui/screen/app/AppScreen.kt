package me.nya_n.notificationnotifier.ui.screen.app

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.common.RequireNotificationPermissionDialog
import me.nya_n.notificationnotifier.ui.common.RequirePackageVisibilityDialog
import me.nya_n.notificationnotifier.ui.screen.about.AboutScreen
import me.nya_n.notificationnotifier.ui.screen.detail.DetailScreen
import me.nya_n.notificationnotifier.ui.screen.license.LicenseScreen
import me.nya_n.notificationnotifier.ui.screen.main.MainScreen
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder

@Composable
fun AppScreen(
    viewModel: AppViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val activity = LocalContext.current as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()

    AppTheme {
        if (uiState.isShowRequirePackageVisibilityDialog) {
            RequirePackageVisibilityDialog(
                onDismissRequest = { isGranted ->
                    if (isGranted) {
                        viewModel.onPackageVisibilityGranted()
                    } else {
                        activity?.finish()
                    }
                }
            )
        }
        if (uiState.isShowRequireNotificationAccessPermissionDialog) {
            RequireNotificationPermissionDialog(
                onDismissRequest = { isGranted ->
                    activity ?: return@RequireNotificationPermissionDialog
                    if (isGranted) {
                        activity.startActivity(
                            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                        )
                    } else {
                        activity.finish()
                    }
                }
            )
        }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.checkPermissions()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Main.name,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it })
            },
        ) {
            composable(Screen.Main.route) { MainScreen(navController) }
            composable(Screen.License.route) { LicenseScreen(navController) }
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
            composable(Screen.About.route) { AboutScreen() }
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
    data object About : Screen("about")

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
