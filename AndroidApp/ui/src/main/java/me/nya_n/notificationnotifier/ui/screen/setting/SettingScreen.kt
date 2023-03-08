package me.nya_n.notificationnotifier.ui.screen.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppColors
import me.nya_n.notificationnotifier.ui.theme.WhiteRippleTheme

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
            .padding(top = 20.dp, bottom = 8.dp)
    )
    CompositionLocalProvider(
        LocalRippleTheme provides WhiteRippleTheme
    ) {
        OutlinedButton(
            onClick = onClickListener,
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = AppColors.RoseBrown
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.notify_test),
                style = TextStyle(color = AppColors.BasicBlack)
            )
        }
    }
}
