package me.nya_n.notificationnotifier.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.*
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/** 詳細画面 */
@Composable
fun DetailScreen(
    navController: NavController,
    app: InstalledApp,
    viewModel: DetailViewModel = getViewModel { parametersOf(app) },
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    SnackbarMessage(
        snackbarHostState = snackbarHostState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    DetailContent(
        snackbarHostState = snackbarHostState,
        app = app,
        condition = uiState.condition,
        onDeleteApp = {
            viewModel.deleteTarget()
            navController.previousBackStackEntry?.apply {
                savedStateHandle["deletedApp"] = app
            }
            navController.popBackStack()
        },
        onConditionChanged = { viewModel.save(it) }
    )
}

/** 詳細画面のコンテンツ本体 */
@Composable
fun DetailContent(
    snackbarHostState: SnackbarHostState,
    app: InstalledApp,
    condition: String,
    onDeleteApp: () -> Unit,
    onConditionChanged: (String) -> Unit
) {
    AppScaffold(snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp),
        ) {
            AppInfo(app, onDeleteApp)
            NotificationSetting(condition, onConditionChanged)
        }
    }
}

/** アプリ情報
 *   - アイコンとアプリ名
 */
@Composable
fun AppInfo(
    app: InstalledApp,
    onDeleteApp: () -> Unit
) {
    Category(name = stringResource(id = R.string.app_info))
    Box(
        modifier = Modifier.padding(vertical = 20.dp)
    ) {
        Row {
            GrayScaleAppIcon(
                app = app,
                modifier = Modifier.size(80.dp),
                isInListView = false
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = app.label)
            }
        }
    }
    AppOutlinedButton(
        text = stringResource(id = R.string.delete),
        onClick = onDeleteApp
    )
}

/** 通知設定
 *   - 条件
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotificationSetting(
    initCondition: String,
    onConditionChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var condition by remember(initCondition) { mutableStateOf(initCondition) }
    Category(name = stringResource(id = R.string.notification_settings))
    /* FIXME: TODO:
     *  テキスト未確定状態で、IMEの◀,▶でカーソルを移動させたとき、カーソルの移動がおかしくなる
     *  TextFieldValueも試したが改善されず
     */
    OutlinedTextField(
        value = condition,
        placeholder = { Text(text = stringResource(id = R.string.condition_hint)) },
        onValueChange = { condition = it },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                // IMEを閉じてフォーカスを消してから保存
                keyboardController?.hide()
                focusManager.clearFocus()
                onConditionChanged(condition)
            }
        ),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = {
            Image(
                imageVector = Icons.Outlined.NotificationsActive,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
    Text(text = stringResource(id = R.string.condition_notice))
}

@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
@Composable
fun DetailPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    AppTheme {
        DetailContent(
            snackbarHostState = snackbarHostState,
            app = InstalledApp("Sample App Name", "example.sample.test"),
            condition = "^.*$",
            onDeleteApp = { },
            onConditionChanged = { }
        )
    }
}

@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
@Composable
fun LongAppNameDetailPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    AppTheme {
        DetailContent(
            snackbarHostState = snackbarHostState,
            app = InstalledApp(
                "Sample App Name So Loooooooooooooooooooong",
                "example.sample.test"
            ),
            condition = "",
            onDeleteApp = { },
            onConditionChanged = { }
        )
    }
}
