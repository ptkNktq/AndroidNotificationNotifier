package me.nya_n.notificationnotifier.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import me.nya_n.notificationnotifier.ui.screen.license.LicenseScreen
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
@ExperimentalPagerApi
fun App() {
    val navController = rememberNavController()
    AppTheme {
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") { MainScreen(navController) }
            composable("license") { LicenseScreen() }
        }
    }
}