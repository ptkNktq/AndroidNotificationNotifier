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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppColors

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun SettingPreview() {
    SettingContent()
}

@Composable
fun SettingScreen() {
    SettingContent()
}

@Composable
fun SettingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        NotifySetting {
            // TODO: 送信テスト
        }
        OtherSetting()
    }
}

/**
 * 通知関係の設定
 *  - 送信先の入力
 *  - 送信テスト
 */
@Composable
fun NotifySetting(
    onClickListener: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    Title(titleResourceId = R.string.settings_general)
    OutlinedTextField(
        value = text,
        placeholder = { Text(text = stringResource(id = R.string.address)) },
        onValueChange = { text = it },
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
        onClick = onClickListener,
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
fun OtherSetting() {
    Title(titleResourceId = R.string.settings_others)
    BasicItem(icon = Icons.Outlined.ReceiptLong, textResourceId = R.string.license) {

    }
}

@Composable
fun Title(@StringRes titleResourceId: Int) {
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

@Composable
fun BasicItem(
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
