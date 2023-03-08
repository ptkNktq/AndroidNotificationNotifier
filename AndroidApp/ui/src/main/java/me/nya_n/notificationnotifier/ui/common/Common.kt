package me.nya_n.notificationnotifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import me.nya_n.notificationnotifier.ui.theme.AppColors

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "NotificationNotifier", color = Color.White) },
        backgroundColor = AppColors.Brown
    )
}

@Composable
fun EmptyView(@StringRes textResourceId: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = textResourceId))
    }
}
