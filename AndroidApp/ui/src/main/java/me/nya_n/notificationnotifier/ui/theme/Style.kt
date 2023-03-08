package me.nya_n.notificationnotifier.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setSystemBarsColor(AppColors.Brown)
    }

    MaterialTheme(content = content)
}

object WhiteRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color {
        return Color.White
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleTheme.defaultRippleAlpha(
            contentColor = LocalContentColor.current,
            lightTheme = MaterialTheme.colors.isLight
        )
    }
}