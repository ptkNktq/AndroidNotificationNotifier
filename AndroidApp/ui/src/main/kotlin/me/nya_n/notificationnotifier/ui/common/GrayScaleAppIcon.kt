package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.util.AppIconCache
import me.nya_n.notificationnotifier.ui.util.isInPreview

@Composable
fun GrayScaleAppIcon(
    app: InstalledApp,
    modifier: Modifier,
    isInListView: Boolean
) {
    if (isInPreview()) {
        Image(
            imageVector = Icons.Default.Android,
            contentDescription = null,
            modifier = modifier
        )
    } else {
        val icon = AppIconCache.get(
            app.packageName,
            LocalContext.current.packageManager,
            isInListView
        )
        Image(
            bitmap = icon.asImageBitmap(),
            contentDescription = null,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
            modifier = modifier
        )
    }
}
