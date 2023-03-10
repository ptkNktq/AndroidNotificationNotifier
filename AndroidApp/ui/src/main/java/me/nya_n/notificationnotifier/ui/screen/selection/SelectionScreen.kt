package me.nya_n.notificationnotifier.ui.screen.selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppList
import me.nya_n.notificationnotifier.ui.common.EmptyView
import me.nya_n.notificationnotifier.ui.common.SnackbarMessage
import me.nya_n.notificationnotifier.ui.theme.AppColors
import org.koin.androidx.compose.getViewModel

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun SelectionPreview() {
    val items = listOf(
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
    )
    SelectionContent(
        items = items,
        onAppSelected = { }
    )
}

/**
 * 通知送信ターゲットアプリの選択画面
 *  - インストール済みのアプリ一覧が表示される
 *  - すでにターゲットに追加してあるものは表示されない
 *  - TODO: システムアプリ等不要な(？)アプリを非表示にする方法を調べる
 */
@Composable
fun SelectionScreen(
    navController: NavController,
    viewModel: SelectionViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    SnackbarMessage(
        scaffoldState = scaffoldState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    SelectionContent(
        items = uiState.items,
        onAppSelected = {
            viewModel.addTarget(it)
            viewModel.loadAppList()
            navController.currentBackStackEntry?.apply {
                savedStateHandle["addedApp"] = it
            }
        }
    )
}

@Composable
fun SelectionContent(
    items: List<InstalledApp>,
    onAppSelected: (InstalledApp) -> Unit
) {
    if (items.isEmpty()) {
        // アプリリストが空
        EmptyView(textResourceId = R.string.no_apps)
    } else {
        Column {
            var text by remember { mutableStateOf("") }
            OutlinedTextField(
                value = text,
                placeholder = { Text(text = stringResource(id = R.string.search_by_app_name)) },
                onValueChange = { text = it },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                ),
                leadingIcon = {
                    Image(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(AppColors.RoseBrown)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )
            AppList(items = items, onAppSelected = onAppSelected)
        }
    }
}
