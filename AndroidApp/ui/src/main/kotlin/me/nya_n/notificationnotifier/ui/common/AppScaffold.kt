package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.theme.AppColors
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun AppScaffold(
    snackbarHostState: SnackbarHostState,
    hasBackContent: Boolean = false,
    onBack: () -> Unit = { },
    bottomBar: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                hasBackContent = hasBackContent,
                onBack = onBack
            )
        },
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.secondary,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                val visuals = it.visuals as? AppSnackbarVisuals
                if (visuals != null) {
                    Snackbar(
                        snackbarData = it,
                        actionColor = Color.White,
                        containerColor = visuals.containerColor,
                        contentColor = Color.White
                    )
                } else {
                    Snackbar(
                        snackbarData = it,
                        actionColor = Color.White,
                        containerColor = AppColors.BasicBlack,
                        contentColor = Color.White
                    )
                }
            }
        },
        modifier = Modifier.systemBarsPadding(),
        content = content
    )
}

@Preview
@Composable
private fun AppScaffoldPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    AppTheme {
        AppScaffold(snackbarHostState) { }
    }
}