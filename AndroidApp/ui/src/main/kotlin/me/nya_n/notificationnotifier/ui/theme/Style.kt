package me.nya_n.notificationnotifier.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material.MaterialTheme as M2

@Composable
@Suppress("UNUSED_PARAMETER")
fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val uiController = rememberSystemUiController()
    MaterialTheme(
        colorScheme = AppColorScheme
    ) {
        val color = MaterialTheme.colorScheme.primary
        SideEffect {
            uiController.setSystemBarsColor(color)
        }
        CompositionLocalProvider(
            LocalRippleTheme provides WhiteRippleTheme,
            content = content
        )
    }
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
            lightTheme = M2.colors.isLight
        )
    }
}