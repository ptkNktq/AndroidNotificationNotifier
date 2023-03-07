package me.nya_n.notificationnotifier.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setSystemBarsColor(AppColor.Brown)
    }

    MaterialTheme(content = content)
}