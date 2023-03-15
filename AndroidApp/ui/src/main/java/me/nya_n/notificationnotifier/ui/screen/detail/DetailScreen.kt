package me.nya_n.notificationnotifier.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import me.nya_n.notificationnotifier.ui.theme.AppColors
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun DetailPreview() {
    DetailContent(
        app = InstalledApp("Sample App Name", "example.sample.test"),
        condition = "^.*$",
        onDeleteApp = { },
        onConditionChanged = { }
    )
}

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun LongAppNameDetailPreview() {
    DetailContent(
        app = InstalledApp(
            "Sample App Name So Loooooooooooooooooooong",
            "example.sample.test"
        ),
        condition = "",
        onDeleteApp = { },
        onConditionChanged = { }
    )
}

/**
 * 詳細画面
 */
@Composable
fun DetailScreen(
    navController: NavController,
    app: InstalledApp,
    viewModel: DetailViewModel = getViewModel { parametersOf(app) },
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val uiState by viewModel.uiState.collectAsState()
    SnackbarMessage(
        scaffoldState = scaffoldState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    DetailContent(
        scaffoldState = scaffoldState,
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

/**
 * 詳細画面のコンテンツ本体
 */
@Composable
fun DetailContent(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    app: InstalledApp,
    condition: String,
    onDeleteApp: () -> Unit,
    onConditionChanged: (String) -> Unit
) {
    AppScaffold(
        scaffoldState = scaffoldState
    ) {
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

/**
 * アプリ情報
 *  - アイコンとアプリ名
 */
@Composable
fun AppInfo(
    app: InstalledApp,
    onDeleteApp: () -> Unit
) {
    Category(titleResourceId = R.string.app_info)
    Box(
        modifier = Modifier.padding(vertical = 20.dp)
    ) {
        Row {
            GrayScaleAppIcon(
                app = app,
                modifier = Modifier.size(80.dp)
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
        R.string.delete,
        onClick = onDeleteApp
    )
}

/**
 * 通知設定
 *  - 条件
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
    Category(titleResourceId = R.string.notification_settings)
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
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        leadingIcon = {
            Image(
                imageVector = Icons.Outlined.NotificationsActive,
                contentDescription = null,
                colorFilter = ColorFilter.tint(AppColors.RoseBrown)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
    Text(text = stringResource(id = R.string.condition_notice))
}