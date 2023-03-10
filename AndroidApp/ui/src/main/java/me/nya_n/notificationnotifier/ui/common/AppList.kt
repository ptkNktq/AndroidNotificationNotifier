package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.theme.AppColors

@Composable
fun AppList(
    items: List<InstalledApp>,
    onAppSelected: (InstalledApp) -> Unit
) {
    LazyColumn {
        items(items) {
            AppListItem(it, onAppSelected)
        }
    }
}

@Composable
fun AppListItem(
    app: InstalledApp,
    onAppSelected: (InstalledApp) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(bounded = true),
                onClick = { onAppSelected(app) }
            )
    ) {
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            GrayScaleAppIcon(
                app = app,
                modifier = Modifier
                    .size(56.dp)
                    .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = app.label, color = AppColors.BasicBlack)
            }
        }
    }
}