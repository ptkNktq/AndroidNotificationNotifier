package me.nya_n.notificationnotifier.ui.common

import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import me.nya_n.notificationnotifier.model.Message

/**
 * Snackbarでメッセージを表示
 */
@Composable
fun SnackbarMessage(
    scaffoldState: ScaffoldState,
    message: Message?,
    onMessageDone: () -> Unit
) {
    message ?: return

    // TODO: Snackbarの色変更に対応
    val (text, _) = when (message) {
        is Message.Error -> {
            Pair(stringResource(id = message.message), MaterialTheme.colorScheme.onSecondary)
        }

        is Message.Notice -> {
            Pair(
                stringResource(id = message.message, formatArgs = message.args),
                MaterialTheme.colorScheme.onSecondary
            )
        }
    }
    LaunchedEffect(scaffoldState.snackbarHostState) {
        scaffoldState.snackbarHostState.showSnackbar(
            message = text,
            actionLabel = "[OK]" // FIXME: ここの文字、i18nどうする？
        )
        onMessageDone()
    }
}
