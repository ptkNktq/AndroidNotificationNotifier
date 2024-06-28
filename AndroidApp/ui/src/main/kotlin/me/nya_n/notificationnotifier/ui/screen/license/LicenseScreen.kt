package me.nya_n.notificationnotifier.ui.screen.license

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.nya_n.notificationnotifier.ui.common.AppScaffold
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun LicenseScreen(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    AppScaffold(
        snackbarHostState = snackbarHostState,
        hasBackContent = true,
        onBack = {
            navController.popBackStack()
        }
    ) {
        AndroidView(
            modifier = Modifier.padding(it),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    loadUrl("file:///android_asset/open_source_licenses.html")
                }
            },
        )
    }
}

@Preview
@Composable
private fun LicensePreview() {
    val navController = rememberNavController()
    AppTheme {
        LicenseScreen(
            navController = navController
        )
    }
}
