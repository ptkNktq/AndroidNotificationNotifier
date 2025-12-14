package me.nya_n.notificationnotifier.ui.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.theme.AppTheme

object Sample {
    val items: List<InstalledApp>
        get() {
            return (0..10).map {
                InstalledApp("Sample App ${it + 1}", "me.nya_n.notificationnotifier")
            }
        }
}

@Composable
fun AppPreview(content: @Composable (SharedTransitionScope, AnimatedVisibilityScope) -> Unit) {
    AppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                content(this@SharedTransitionLayout, this@AnimatedVisibility)
            }
        }
    }
}