package me.nya_n.notificationnotifier.ui.screen.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import me.nya_n.notificationnotifier.ui.common.BubbleView

@Composable
fun AboutScreen() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    AboutContent(screenWidth, screenHeight)
}

@Composable
fun AboutContent(
    screenWidth: Float,
    screenHeight: Float
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            BubbleView(context, screenWidth, screenHeight)
        }
    )
}