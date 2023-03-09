package me.nya_n.notificationnotifier.ui.screen.setting

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppColors
import org.koin.androidx.compose.getViewModel

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun SettingPreview() {
    SettingContent(
        uiState = UiState(address = "192.168.11.2:5555"),
        onValueChange = { },
        onNotifyTest = { }
    )
}

/**
 * 設定画面
 * TODO:
 *  - 設定バックアップ/復元機能
 */
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    SnackbarMessage(
        scaffoldState = scaffoldState,
        uiState = uiState
    ) {
        viewModel.messageShown()
    }
    SettingContent(
        navController = navController,
        uiState = uiState,
        onValueChange = { viewModel.updateAddress(it) },
        onNotifyTest = { viewModel.notifyTest() }
    )
}

/**
 * 設定画面のコンテンツ本体
 */
@Composable
fun SettingContent(
    navController: NavController = rememberNavController(),
    uiState: UiState,
    onValueChange: (String) -> Unit,
    onNotifyTest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        NotifySetting(
            uiState,
            onValueChange = onValueChange,
            onNotifyTest = onNotifyTest
        )
        OtherSetting(navController)
    }
}

/**
 * 通知関係の設定
 *  - 送信先の入力
 *  - 送信テスト
 */
@Composable
fun NotifySetting(
    uiState: UiState,
    onValueChange: (String) -> Unit,
    onNotifyTest: () -> Unit
) {
    Category(titleResourceId = R.string.settings_general)
    OutlinedTextField(
        value = uiState.address,
        placeholder = { Text(text = stringResource(id = R.string.address)) },
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        leadingIcon = {
            Image(
                imageVector = Icons.Outlined.Devices,
                contentDescription = null,
                colorFilter = ColorFilter.tint(AppColors.RoseBrown)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
    OutlinedButton(
        onClick = onNotifyTest,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = AppColors.RoseBrown
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.notify_test),
            style = TextStyle(color = AppColors.BasicBlack)
        )
    }
}

/**
 * その他の項目
 *  - ライセンス表示
 *  - バージョン表示
 */
@Composable
fun OtherSetting(
    navController: NavController
) {
    Category(titleResourceId = R.string.settings_others)
    ClickableBasicItem(icon = Icons.Outlined.ReceiptLong, textResourceId = R.string.license) {
        navController.navigate("license")
    }
}

/**
 * 設定のカテゴリ
 */
@Composable
fun Category(@StringRes titleResourceId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Box(
            modifier = Modifier.size(24.dp, 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Divider(color = AppColors.BasicBlack)
        }
        Text(
            text = stringResource(id = titleResourceId),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = TextStyle(color = AppColors.BasicBlack)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Divider(color = AppColors.BasicBlack)
        }
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
            colorFilter = ColorFilter.tint(AppColors.BasicBlack)
        )
        Text(
            text = stringResource(id = textResourceId),
            style = TextStyle(color = AppColors.BasicBlack),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.fillMaxWidth())
    }
}

/**
 * Snackbarでメッセージを表示
 */
@Composable
fun SnackbarMessage(
    scaffoldState: ScaffoldState,
    uiState: UiState,
    onMessageDone: () -> Unit
) {
    uiState.message?.let {
        // TODO: Snackbarの色変更に対応
        val (message, color) = when (it) {
            is Message.Error -> {
                Pair(stringResource(id = it.message), AppColors.BasicBlack)
            }
            is Message.Notice -> {
                Pair(stringResource(id = it.message, formatArgs = it.args), AppColors.BasicBlack)
            }
        }
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "[OK]" // FIXME: ここの文字、i18nどうする？
            )
            onMessageDone()
        }
    }
}