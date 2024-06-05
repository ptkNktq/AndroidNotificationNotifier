package me.nya_n.notificationnotifier.ui.common

import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.content_title),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary
    )
}

@Preview
@Composable
fun TopBarPreview() {
    AppTheme {
        TopBar()
    }
}