package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.theme.AppColors

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun AppListPreview() {
    val items = listOf(
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp(
            "Sample App Name So Looooooooooooooooong",
            "me.nya_n.notificationnotifier"
        ),
    )
    AppList(
        items = items,
        onAppSelected = { }
    )
}

@Composable
fun AppList(
    items: List<InstalledApp>,
    onAppSelected: (InstalledApp) -> Unit
) {
    LazyColumn {
        items(
            count = items.size,
            key = { "($it)${items[it]}" },
            itemContent = { AppListItem(app = items[it], onAppSelected = onAppSelected) }
        )
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
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(bounded = true),
                onClick = { onAppSelected(app) }
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            GrayScaleAppIcon(
                app = app,
                modifier = Modifier.size(56.dp),
                isInListView = true
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = app.label,
                    color = AppColors.BasicBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}