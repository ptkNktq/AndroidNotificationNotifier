package me.nya_n.notificationnotifier.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary
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
private fun AppOutlinedButtonPreview() {
    AppTheme {
        AppOutlinedButton("text") { }
    }
}