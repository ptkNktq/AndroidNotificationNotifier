package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun AppScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    bottomBar: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        topBar = { TopBar() },
        bottomBar = bottomBar,
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(snackbarData = data, actionColor = Color.White)
            }
        },
        modifier = Modifier.systemBarsPadding(),
        content = content
    )
}

@Preview
@Composable
fun AppScaffoldPreview() {
    AppTheme {
        AppScaffold { }
    }
}