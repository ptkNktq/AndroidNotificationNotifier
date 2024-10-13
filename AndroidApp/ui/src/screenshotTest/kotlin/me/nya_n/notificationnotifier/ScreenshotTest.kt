package me.nya_n.notificationnotifier

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.screen.detail.DetailContent
import me.nya_n.notificationnotifier.ui.screen.main.MainContent
import me.nya_n.notificationnotifier.ui.screen.main.TabItem
import me.nya_n.notificationnotifier.ui.screen.selection.SelectionContent
import me.nya_n.notificationnotifier.ui.screen.settings.SettingsContent
import me.nya_n.notificationnotifier.ui.screen.target.TargetContent
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import me.nya_n.notificationnotifier.ui.util.Sample

class ScreenshotTest {

    // region: 画面レベル

    @Preview
    @Composable
    fun MainContentTest() {
        val snackbarHostState = remember { SnackbarHostState() }
        val tabItems = listOf(
            TabItem("タブ1", Icons.Outlined.NotificationsActive),
            TabItem("タブ2", Icons.AutoMirrored.Rounded.List),
            TabItem("タブ3", Icons.Outlined.Settings),
        )
        val pagerState = rememberPagerState(pageCount = { tabItems.size })
        AppTheme {
            MainContent(
                snackbarHostState = snackbarHostState,
                tabItems = tabItems,
                pagerState = pagerState
            ) { }
        }
    }


    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun TargetContentTest() {
        val items = listOf(
            InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
            InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
            InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
        )
        AppTheme {
            TargetContent(
                items = items,
                isLoading = false,
                onAppSelected = { }
            )
        }
    }

    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun DetailContentTest() {
        val snackbarHostState = remember { SnackbarHostState() }
        AppTheme {
            DetailContent(
                snackbarHostState = snackbarHostState,
                app = InstalledApp("Sample App Name", "example.sample.test"),
                condition = "^.*$",
                onBack = { },
                onDeleteApp = { },
                onConditionChanged = { }
            )
        }
    }

    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun SelectionContentTest() {
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

    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun SettingsContentTest() {
        AppTheme {
            SettingsContent(
                address = "192.168.11.2:5555",
                versionCode = 1,
                versionName = "1.0",
                onValueChange = { },
                onNotifyTest = { },
                onExportData = { },
                onImportData = { },
                onLicense = { },
                onAboutDeveloper = { }
            )
        }
    }

    // endregion

    // region: コンポーネントレベル
}