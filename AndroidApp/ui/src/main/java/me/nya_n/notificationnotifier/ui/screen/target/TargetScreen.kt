package me.nya_n.notificationnotifier.ui.screen.target

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppList
import me.nya_n.notificationnotifier.ui.common.EmptyView

@Composable
@Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
fun TargetPreview() {
    val items = listOf(
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
    )
    TargetContent(items)
}

/**
 * 通知送信ターゲットに追加したアプリリスト
 */
@Composable
fun TargetScreen() {
    val items: List<InstalledApp> = listOf() // TODO: ViewModelから取得
    TargetContent(items = items)
}

@Composable
fun TargetContent(
    items: List<InstalledApp>
) {
    if (items.isEmpty()) {
        EmptyView(textResourceId = R.string.no_apps)
    } else {
        AppList(items = items) {
            // TODO: アプリを選択されたときの処理 (詳細画面へ)
        }
    }
}