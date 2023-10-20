package me.nya_n.notificationnotifier.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

object AppColors {
    // 配色アイデア手帖 p186 レシピ073 をベースにした色
    val gray = Color(0xFFCECECE)
    val RoseBrown = Color(0xFFC7B5A8)
    val Brown = Color(0xFF443731)
    val LightBrown = Color(0xFF817167)
    val BasicBlack = Color(0xFF352E2B)
}

val AppColorScheme = darkColorScheme(
    primary = AppColors.Brown,
    onPrimary = Color.White,
    secondary = AppColors.RoseBrown,
    onSecondary = AppColors.BasicBlack,
    secondaryContainer = AppColors.LightBrown,
    onSecondaryContainer = Color.White,
    surface = AppColors.Brown,
    onSurface = Color.White,
    onSurfaceVariant = AppColors.gray
)