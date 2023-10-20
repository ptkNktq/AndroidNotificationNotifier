package me.nya_n.notificationnotifier.ui.screen.selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppList
import me.nya_n.notificationnotifier.ui.common.EmptyView
import me.nya_n.notificationnotifier.ui.common.SnackbarMessage
import me.nya_n.notificationnotifier.ui.util.Sample
import org.koin.androidx.compose.getViewModel

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun SelectionPreview() {
    SelectionContent(
        items = Sample.items,
        onAppSelected = { },
        initQuery = "",
        onQueryInputted = { }
    )
}

/**
 * 通知送信ターゲットアプリの選択画面
 *  - インストール済みのアプリ一覧が表示される
 *  - すでにターゲットに追加してあるものは表示されない
 *  - TODO: システムアプリ等不要な(？)アプリを非表示にする方法を調べる
 */
@Composable
fun SelectionScreen(
    viewModel: SelectionViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadAppList()
    }
    SnackbarMessage(
        scaffoldState = scaffoldState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    SelectionContent(
        items = uiState.items,
        onAppSelected = {
            viewModel.addTarget(it)
            viewModel.loadAppList()
        },
        initQuery = uiState.query,
        onQueryInputted = { viewModel.searchApp(it) }
    )
}

@Composable
fun SelectionContent(
    items: List<InstalledApp>,
    onAppSelected: (InstalledApp) -> Unit,
    initQuery: String,
    onQueryInputted: (String) -> Unit
) {
    Column {
        QueryTextField(initQuery = initQuery, onQueryInputted = onQueryInputted)
        if (items.isEmpty()) {
            // アプリリストが空
            EmptyView(textResourceId = R.string.no_apps)
        } else {
            AppList(items = items, onAppSelected = onAppSelected)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QueryTextField(
    initQuery: String,
    onQueryInputted: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf(initQuery) }
    OutlinedTextField(
        value = query,
        placeholder = { Text(text = stringResource(id = R.string.search_by_app_name)) },
        onValueChange = { query = it },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                // IMEを閉じてフォーカスを消してから検索
                keyboardController?.hide()
                focusManager.clearFocus()
                onQueryInputted(query)
            }
        ),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = {
            Image(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp)
    )
}
