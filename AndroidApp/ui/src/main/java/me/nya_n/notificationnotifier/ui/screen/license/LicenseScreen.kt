package me.nya_n.notificationnotifier.ui.screen.license

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
@Preview
fun LicensePreview() {
    LicenseScreen()
}

@Composable
fun LicenseScreen() {
    AndroidView(
        factory = {
            WebView(it).apply {
                webViewClient = WebViewClient()
                loadUrl("file:///android_asset/licenses.html")
            }
        },
    )
}
