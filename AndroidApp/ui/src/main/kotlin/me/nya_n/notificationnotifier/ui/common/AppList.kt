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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun AppList(
    items: List<InstalledApp>,
    emptyMessage: String,
    onAppSelected: (InstalledApp) -> Unit
) {
    if (items.isEmpty()) {
        EmptyView(message = emptyMessage)
    } else {
        LazyColumn {
            items(
                count = items.size,
                key = { "($it)${items[it]}" },
                itemContent = { AppListItem(app = items[it], onAppSelected = onAppSelected) }
            )
        }
    }
}

@Composable
fun AppListItem(
    app: InstalledApp,
    onAppSelected: (InstalledApp) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                onClick = { onAppSelected(app) }
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
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
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
@Composable
fun AppListPreview() {
    val items = listOf(
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp(
            "Sample App Name So Looooooooooooooooong",
            "me.nya_n.notificationnotifier"
        ),
    )
    AppTheme {
        AppList(
            items = items,
            emptyMessage = "empty",
            onAppSelected = { }
        )
    }
}

@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
@Composable
fun EmptyAppListPreview() {
    AppTheme {
        AppList(
            items = emptyList(),
            emptyMessage = "empty",
            onAppSelected = { }
        )
    }
}
