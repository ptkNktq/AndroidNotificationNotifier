package me.nya_n.notificationnotifier.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun CommonDialog(
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        text = { Text(text = message) },
        onDismissRequest = { /* noop */ },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = positiveButtonText,
                    style = TextStyle(
                        color = Color.White
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = negativeButtonText,
                    style = TextStyle(
                        color = Color.White
                    )
                )
            }
        }
    )
}

@Composable
fun RequireNotificationPermissionDialog(
    onDismissRequest: () -> Unit
) {
    CommonDialog(
        message = stringResource(id = R.string.require_permission),
        positiveButtonText = stringResource(id = R.string.next),
        negativeButtonText = stringResource(id = R.string.ng),
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun RequirePackageVisibilityDialog(
    onDismissRequest: () -> Unit
) {
    CommonDialog(
        message = stringResource(id = R.string.require_package_visibility),
        positiveButtonText = stringResource(id = R.string.approval),
        negativeButtonText = stringResource(id = R.string.ng),
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
fun CommonDialogPreview() {
    AppTheme {
        CommonDialog(
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            positiveButtonText = "YES",
            negativeButtonText = "NO",
            onDismissRequest = { }
        )
    }
}

@Preview
@Composable
fun RequireNotificationPermissionDialogPreview() {
    AppTheme {
        RequireNotificationPermissionDialog(
            onDismissRequest = { }
        )
    }
}

@Preview
@Composable
fun RequirePackageVisibilityDialogPreview() {
    AppTheme {
        RequirePackageVisibilityDialog(
            onDismissRequest = { }
        )
    }
}
