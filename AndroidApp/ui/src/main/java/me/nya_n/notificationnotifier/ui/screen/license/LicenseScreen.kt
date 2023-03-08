package me.nya_n.notificationnotifier.ui.screen.license

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import me.nya_n.notificationnotifier.ui.common.TopBar

@Composable
@Preview
fun LicensePreview() {
    LicenseScreen()
}

@Composable
fun LicenseScreen() {
    Scaffold(
        topBar = { TopBar() }
    ) {
        AndroidView(
            modifier = Modifier.padding(it),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    loadUrl("file:///android_asset/licenses.html")
                }
            },
        )
    }
}
