package me.nya_n.notificationnotifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppTheme

/**
 * 表示するアイテムがなかったときに表示するView
 */
@Composable
fun EmptyView(@StringRes textResourceId: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = textResourceId))
    }
}

@Preview
@Composable
fun EmptyViewPreview() {
    AppTheme {
        EmptyView(textResourceId = R.string.no_contents)
    }
}