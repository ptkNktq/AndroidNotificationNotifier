package me.nya_n.notificationnotifier.ui.screen.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.nya_n.notificationnotifier.model.Backup
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppOutlinedButton
import me.nya_n.notificationnotifier.ui.common.Category
import me.nya_n.notificationnotifier.ui.common.SnackbarMessage
import org.koin.androidx.compose.getViewModel

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun SettingsPreview() {
    SettingsContent(
        uiState = UiState(address = "192.168.11.2:5555"),
        onValueChange = { },
        onNotifyTest = { },
        onExportData = { },
        onImportData = { }
    )
}

/**
 * 設定画面
 * TODO:
 *  - 設定バックアップ/復元機能
 */
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val uiEvents by viewModel.uiEvent.collectAsState()
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != AppCompatActivity.RESULT_OK) return@rememberLauncherForActivityResult
        viewModel.exportData(context, it.data?.data)
    }
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != AppCompatActivity.RESULT_OK) return@rememberLauncherForActivityResult
        viewModel.importData(context, it.data?.data)
    }
    uiEvents.firstOrNull()?.let {
        when (it) {
            is UiEvent.ExportData -> {
                LaunchedEffect(Unit) {
                    exportLauncher.launch(
                        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/json"
                            putExtra(Intent.EXTRA_TITLE, Backup.FILE_NAME)
                        }
                    )
                }
            }

            is UiEvent.ImportData -> {
                LaunchedEffect(Unit) {
                    importLauncher.launch(
                        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/json"
                        }
                    )
                }
            }
        }
        viewModel.consumeEvent(it)
    }
    SnackbarMessage(
        scaffoldState = scaffoldState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    SettingsContent(
        navController = navController,
        uiState = uiState,
        onValueChange = { viewModel.updateAddress(it) },
        onNotifyTest = { viewModel.notifyTest() },
        onExportData = { viewModel.event(UiEvent.ExportData()) },
        onImportData = { viewModel.event(UiEvent.ImportData()) }
    )
}

/**
 * 設定画面のコンテンツ本体
 */
@Composable
fun SettingsContent(
    navController: NavController = rememberNavController(),
    uiState: UiState,
    onValueChange: (String) -> Unit,
    onNotifyTest: () -> Unit,
    onExportData: () -> Unit,
    onImportData: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        NotifySettings(
            uiState,
            onValueChange = onValueChange,
            onNotifyTest = onNotifyTest
        )
        OtherSettings(
            navController = navController,
            onExportData = onExportData,
            onImportData = onImportData
        )
    }
}

/**
 * 通知関係の設定
 *  - 送信先の入力
 *  - 送信テスト
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotifySettings(
    uiState: UiState,
    onValueChange: (String) -> Unit,
    onNotifyTest: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Category(titleResourceId = R.string.settings_general)
    /* FIXME: TODO:
     *  英数記号のみに入力制限したいが適切なものがなかったのでKeyboardType.Emailで妥協
     *  とりあえず最初に表示されるIMEが英数になる
     */
    OutlinedTextField(
        value = uiState.address,
        placeholder = { Text(text = stringResource(id = R.string.address)) },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
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
                imageVector = Icons.Outlined.Devices,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
    AppOutlinedButton(
        R.string.notify_test,
        onClick = onNotifyTest
    )
}

/**
 * その他の項目
 *  - ライセンス表示
 *  - バージョン表示
 */
@Composable
fun OtherSettings(
    navController: NavController,
    onExportData: () -> Unit,
    onImportData: () -> Unit
) {
    Category(titleResourceId = R.string.settings_others)
    ClickableBasicItem(icon = Icons.Outlined.CloudUpload, textResourceId = R.string.export_data) {
        onExportData()
    }
    ClickableBasicItem(icon = Icons.Outlined.CloudDownload, textResourceId = R.string.import_data) {
        onImportData()
    }
    ClickableBasicItem(icon = Icons.Outlined.ReceiptLong, textResourceId = R.string.license) {
        navController.navigate("license")
    }
}

/**
 * 基本的な項目
 *  - [アイコン 項目名] で構成される
 *  - クリックできる
 */
@Composable
fun ClickableBasicItem(
    icon: ImageVector,
    @StringRes textResourceId: Int,
    onClickListener: () -> Unit
) {
    TextButton(
        onClick = onClickListener,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
        )
        Text(
            text = stringResource(id = textResourceId),
            style = TextStyle(color = MaterialTheme.colorScheme.onSecondary),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.fillMaxWidth())
    }
}
