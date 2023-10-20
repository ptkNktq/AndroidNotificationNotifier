package me.nya_n.notificationnotifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppTheme

/**
 * カテゴリ
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
            Divider(color = MaterialTheme.colorScheme.onSecondary)
        }
        Text(
            text = stringResource(id = titleResourceId),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = TextStyle(color = MaterialTheme.colorScheme.onSecondary)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Divider(color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

@Preview
@Composable
fun CategoryPreview() {
    AppTheme {
        Category(titleResourceId = R.string.settings_general)
    }
}