package me.nya_n.notificationnotifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.util.AppIcon
import me.nya_n.notificationnotifier.ui.util.isInPreview

@Composable
fun AppScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    bottomBar: @Composable () -> Unit = { },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.secondary,
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
        title = {
            Text(
                text = stringResource(id = R.string.content_title),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary
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

@Composable
fun AppOutlinedButton(
    @StringRes textResourceId: Int,
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
            text = stringResource(id = textResourceId),
            style = TextStyle(color = MaterialTheme.colorScheme.onSecondary)
        )
    }
}

@Composable
fun GrayScaleAppIcon(
    app: InstalledApp,
    modifier: Modifier,
    isInListView: Boolean
) {
    if (isInPreview()) {
        Image(
            imageVector = Icons.Default.Android,
            contentDescription = null,
            modifier = modifier
        )
    } else {
        val icon = AppIcon.get(
            app.packageName,
            LocalContext.current.packageManager,
            isInListView
        )
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
