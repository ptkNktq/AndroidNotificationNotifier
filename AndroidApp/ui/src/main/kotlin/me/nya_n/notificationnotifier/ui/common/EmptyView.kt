package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.theme.AppTheme

/** 表示するアイテムがなかったときに表示するView */
@Composable
fun EmptyView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}

@Preview
@Composable
fun EmptyViewPreview() {
    AppTheme {
        EmptyView("empty")
    }
}