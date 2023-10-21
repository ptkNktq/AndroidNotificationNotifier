package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = text,
            style = TextStyle(color = MaterialTheme.colorScheme.onSecondary)
        )
    }
}

@Preview
@Composable
fun AppOutlinedButtonPreview() {
    AppTheme {
        AppOutlinedButton("text") { }
    }
}