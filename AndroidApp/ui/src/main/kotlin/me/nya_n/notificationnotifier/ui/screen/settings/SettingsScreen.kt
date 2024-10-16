package me.nya_n.notificationnotifier.ui.screen.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import me.nya_n.notificationnotifier.model.Backup
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppOutlinedButton
import me.nya_n.notificationnotifier.ui.common.Category
import me.nya_n.notificationnotifier.ui.common.SnackbarMessage
import me.nya_n.notificationnotifier.ui.screen.app.Screen
import me.nya_n.notificationnotifier.ui.theme.AppColors
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

/** 設定画面 */
@Composable
fun SettingsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: SettingsViewModel = koinViewModel()
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
        snackbarHostState = snackbarHostState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    SettingsContent(
        address = uiState.address,
        versionCode = uiState.appConfig.versionCode,
        versionName = uiState.appConfig.versionString,
        onValueChange = { viewModel.updateAddress(it) },
        onNotifyTest = { viewModel.notifyTest() },
        onExportData = { viewModel.event(UiEvent.ExportData()) },
        onImportData = { viewModel.event(UiEvent.ImportData()) },
        onLicense = { navController.navigate(Screen.License.route) },
        onAboutDeveloper = { navController.navigate(Screen.About.route) }
    )
}

/** 設定画面のコンテンツ本体 */
@Composable
fun SettingsContent(
    address: String,
    versionCode: Int,
    versionName: String,
    onValueChange: (String) -> Unit,
    onNotifyTest: () -> Unit,
    onExportData: () -> Unit,
    onImportData: () -> Unit,
    onLicense: () -> Unit,
    onAboutDeveloper: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            NotifySettings(
                address = address,
                onValueChange = onValueChange,
                onNotifyTest = onNotifyTest
            )
            OtherSettings(
                onExportData = onExportData,
                onImportData = onImportData,
                onLicense = onLicense,
                onAboutDeveloper = onAboutDeveloper
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            text = "v$versionName($versionCode)",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}

/** 通知関係の設定
 *   - 送信先の入力
 *   - 送信テスト
 */
@Composable
private fun NotifySettings(
    address: String,
    onValueChange: (String) -> Unit,
    onNotifyTest: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Category(name = stringResource(id = R.string.settings_general))
    /* FIXME: TODO:
     *  英数記号のみに入力制限したいが適切なものがなかったのでKeyboardType.Emailで妥協
     *  とりあえず最初に表示されるIMEが英数になる
     */
    OutlinedTextField(
        value = address,
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
        colors = AppColors.outlinedTextFieldColors(),
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
        text = stringResource(id = R.string.notify_test),
        onClick = onNotifyTest
    )
}

/** その他の項目
 *   - ライセンス表示
 *   - バージョン表示
 */
@Composable
private fun OtherSettings(
    onExportData: () -> Unit,
    onImportData: () -> Unit,
    onLicense: () -> Unit,
    onAboutDeveloper: () -> Unit
) {
    Category(name = stringResource(id = R.string.settings_others))
    ClickableBasicItem(
        icon = Icons.Outlined.CloudUpload,
        text = stringResource(id = R.string.export_data),
        onClickListener = onExportData
    )
    ClickableBasicItem(
        icon = Icons.Outlined.CloudDownload,
        text = stringResource(id = R.string.import_data),
        onClickListener = onImportData
    )
    ClickableBasicItem(
        icon = Icons.AutoMirrored.Outlined.ReceiptLong,
        text = stringResource(id = R.string.license),
        onClickListener = onLicense
    )
    ClickableBasicItem(
        icon = Icons.Outlined.Code,
        text = stringResource(id = R.string.about),
        onClickListener = onAboutDeveloper
    )
}

/** 基本的な項目
 *   - [アイコン 項目名] で構成される
 *   - クリックできる
 */
@Composable
private fun ClickableBasicItem(
    icon: ImageVector,
    text: String,
    onClickListener: () -> Unit
) {
    TextButton(
        onClick = onClickListener,
        shape = RoundedCornerShape(4.dp),
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
            text = text,
            style = TextStyle(color = MaterialTheme.colorScheme.onSecondary),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.fillMaxWidth())
    }
}

@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
@Composable
private fun SettingsPreview() {
    AppTheme {
        SettingsContent(
            address = "192.168.11.2:5555",
            versionCode = 1,
            versionName = "1.0",
            onValueChange = { },
            onNotifyTest = { },
            onExportData = { },
            onImportData = { },
            onLicense = { },
            onAboutDeveloper = { }
        )
    }
}
