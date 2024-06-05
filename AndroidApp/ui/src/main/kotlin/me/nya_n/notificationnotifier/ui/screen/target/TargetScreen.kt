package me.nya_n.notificationnotifier.ui.screen.target

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppList
import me.nya_n.notificationnotifier.ui.common.SnackbarMessage
import me.nya_n.notificationnotifier.ui.screen.Screen
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

/** 通知送信ターゲットに追加したアプリリスト */
@Composable
fun TargetScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: TargetViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadTargets()
    }
    SnackbarMessage(
        snackbarHostState = snackbarHostState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    TargetContent(items = uiState.items) {
        navController.navigate(Screen.Detail.createRouteWithParams(listOf(it)))
    }
}

@Composable
fun TargetContent(
    items: List<InstalledApp>,
    onAppSelected: (InstalledApp) -> Unit
) {
    AppList(
        items = items,
        emptyMessage = stringResource(id = R.string.no_apps),
        onAppSelected = onAppSelected
    )
}

@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
@Composable
fun TargetPreview() {
    val items = listOf(
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
    )
    AppTheme {
        TargetContent(items = items) { }
    }
}
