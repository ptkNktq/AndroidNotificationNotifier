package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.theme.AppColors
import me.nya_n.notificationnotifier.ui.util.AppIcon
import me.nya_n.notificationnotifier.ui.util.isInPreview

@Composable
fun AppList(
    items: List<InstalledApp>,
    onItemClickedListener: (InstalledApp) -> Unit
) {
    val itemView: @Composable (InstalledApp) -> Unit = if (isInPreview()) {
        { PreviewItem(it) { onItemClickedListener(it) } }
    } else {
        { AppListItem(it) { onItemClickedListener(it) } }
    }
    LazyColumn {
        items(items) {
            itemView(it)
        }
    }
}

@Composable
fun AppListItem(
    item: InstalledApp,
    onItemClickedListener: () -> Unit
) {
    val icon = AppIcon.get(item.packageName, LocalContext.current.packageManager)
    Item(
        icon = {
            Image(
                bitmap = icon.asImageBitmap(),
                contentDescription = null,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                modifier = it
            )
        },
        text = item.label,
        onItemClickedListener
    )
}

/**
 * PreviewではpackageManagerにアクセスできないのでその代替ItemView
 * Imageに渡すリソースがBitmapかImageVectorかの違いでAppListItemとだいたい一緒、代替だけにｗ
 */
@Composable
fun PreviewItem(
    item: InstalledApp,
    onItemClickedListener: () -> Unit
) {
    Item(
        icon = {
            Image(
                imageVector = Icons.Default.Android,
                contentDescription = null,
                modifier = it
            )
        },
        text = item.label,
        onItemClickedListener
    )
}

@Composable
fun Item(
    icon: @Composable (Modifier) -> Unit,
    text: String,
    onItemClickedListener: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(bounded = true),
                onClick = onItemClickedListener
            )
    ) {
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            icon(
                Modifier
                    .size(56.dp)
                    .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = text, color = AppColors.BasicBlack)
            }
        }
    }
}