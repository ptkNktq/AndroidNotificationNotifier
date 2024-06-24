package me.nya_n.notificationnotifier.ui.screen.selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import me.nya_n.notificationnotifier.ui.common.SnackbarMessage
import me.nya_n.notificationnotifier.ui.theme.AppColors
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import me.nya_n.notificationnotifier.ui.util.Sample
import org.koin.androidx.compose.koinViewModel

/** 通知送信ターゲットアプリの選択画面
 *   - インストール済みのアプリ一覧が表示される
 *   - すでにターゲットに追加してあるものは表示されない
 *   - TODO: システムアプリ等不要な(？)アプリを非表示にする方法を調べる
 */
@Composable
fun SelectionScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: SelectionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadAppList()
    }
    SnackbarMessage(
        snackbarHostState = snackbarHostState,
        message = uiState.message
    ) {
        viewModel.messageShown()
    }
    SelectionContent(
        items = uiState.items,
        isLoading = uiState.isLoading,
        initQuery = uiState.query,
        onAppSelected = {
            viewModel.addTarget(it)
            viewModel.loadAppList()
        },
        onQueryInputted = { viewModel.searchApp(it) }
    )
}

@Composable
private fun SelectionContent(
    items: List<InstalledApp>,
    isLoading: Boolean,
    initQuery: String,
    onAppSelected: (InstalledApp) -> Unit,
    onQueryInputted: (String) -> Unit
) {
    Column {
        QueryTextField(initQuery = initQuery, onQueryInputted = onQueryInputted)
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            AppList(
                items = items,
                onAppSelected = onAppSelected
            )
        }
    }
}

@Composable
private fun QueryTextField(
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
        colors = AppColors.OutlinedTextFieldColors(),
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

@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
@Composable
private fun SelectionPreview() {
    AppTheme {
        SelectionContent(
            items = Sample.items,
            isLoading = false,
            onAppSelected = { },
            initQuery = "",
            onQueryInputted = { }
        )
    }
}
