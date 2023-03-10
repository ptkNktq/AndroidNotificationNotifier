package me.nya_n.notificationnotifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.theme.AppColors
import me.nya_n.notificationnotifier.ui.util.AppIcon
import me.nya_n.notificationnotifier.ui.util.isInPreview

@Composable
fun AppScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    bottomBar: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        backgroundColor = AppColors.RoseBrown,
        topBar = { TopBar() },
        bottomBar = bottomBar,
        scaffoldState = scaffoldState,
        modifier = Modifier.systemBarsPadding(),
        content = content
    )
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "NotificationNotifier", color = Color.White) },
        backgroundColor = AppColors.Brown
    )
}

/**
 * 表示するアイテムがなかったときに表示するView
 */
@Composable
fun EmptyView(@StringRes textResourceId: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = textResourceId))
    }
}

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
fun AppOutlinedButton(
    @StringRes textResourceId: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = AppColors.RoseBrown
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = textResourceId),
            style = TextStyle(color = AppColors.BasicBlack)
        )
    }
}

@Composable
fun GrayScaleAppIcon(
    app: InstalledApp,
    modifier: Modifier
) {
    if (isInPreview()) {
        Image(
            imageVector = Icons.Default.Android,
            contentDescription = null,
            modifier = modifier
        )
    } else {
        val icon = AppIcon.get(app.packageName, LocalContext.current.packageManager)
        Image(
            bitmap = icon.asImageBitmap(),
            contentDescription = null,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
            modifier = modifier
        )
    }
}

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
    val (text, color) = when (message) {
        is Message.Error -> {
            Pair(stringResource(id = message.message), AppColors.BasicBlack)
        }
        is Message.Notice -> {
            Pair(
                stringResource(id = message.message, formatArgs = message.args),
                AppColors.BasicBlack
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
