package me.nya_n.notificationnotifier.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppColors

/** Snackbarでメッセージを表示 */
@Composable
fun SnackbarMessage(
    snackbarHostState: SnackbarHostState,
    message: Message?,
    onMessageShown: () -> Unit
) {
    message ?: return

    val visuals = when (message) {
        is Message.Error -> {
            AppSnackbarVisuals(
                message = stringResource(id = message.message),
                containerColor = MaterialTheme.colorScheme.error,
                actionLabel = stringResource(id = R.string.ok)
            )
        }

        is Message.Notice -> {
            AppSnackbarVisuals(
                message = stringResource(id = message.message, formatArgs = message.args),
                containerColor = AppColors.BasicBlack,
                actionLabel = stringResource(id = R.string.ok)
            )
        }
    }
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(visuals)
        onMessageShown()
    }
}

/** Snackbarの見た目変更
 *   - overrideしている変数のデフォルト値は[SnackbarHostState.SnackbarVisualsImpl]と同じ
 *  @param containerColor 背景色
 */
data class AppSnackbarVisuals(
    override val message: String,
    val containerColor: Color,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration =
        if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
) : SnackbarVisuals