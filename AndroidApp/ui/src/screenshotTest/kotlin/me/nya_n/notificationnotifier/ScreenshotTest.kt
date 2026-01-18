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
import com.android.tools.screenshot.PreviewTest
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.common.AppList
import me.nya_n.notificationnotifier.ui.common.AppOutlinedButton
import me.nya_n.notificationnotifier.ui.common.AppScaffold
import me.nya_n.notificationnotifier.ui.common.Category
import me.nya_n.notificationnotifier.ui.common.CommonDialog
import me.nya_n.notificationnotifier.ui.common.EmptyView
import me.nya_n.notificationnotifier.ui.common.RequireNotificationPermissionDialog
import me.nya_n.notificationnotifier.ui.common.RequirePackageVisibilityDialog
import me.nya_n.notificationnotifier.ui.common.TopBar
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

    @PreviewTest
    @Preview
    @Composable
    fun MainContentScreenshotTest() {
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

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun TargetContentScreenshotTest() {
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

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun DetailContentScreenshotTest() {
        AppTheme {
            DetailContent(
                app = InstalledApp("Sample App Name", "example.sample.test"),
                onDeleteApp = { },
                isIgnoreSummary = false,
                onIgnoreSummaryChanged = { },
                condition = "^.*$",
                onConditionChanged = { }
            )
        }
    }

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun SelectionContentScreenshotTest() {
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

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun SettingsContentScreenshotTest() {
        AppTheme {
            SettingsContent(
                address = "192.168.11.2:5555",
                versionCode = 1,
                versionName = "1.0",
                onValueChange = { },
                onNotifyTest = { },
                isWifiOnlyNotificationEnabled = false,
                onWifiOnlySettingChanged = { },
                onExportData = { },
                onImportData = { },
                onLicense = { },
                onAboutDeveloper = { }
            )
        }
    }

    // endregion

    // region: コンポーネントレベル

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun AppListComponentScreenshotTest() {
        val items = listOf(
            InstalledApp("Sample App", "me.nya_n.notificationnotifier"),
            InstalledApp(
                "Sample App Name So Looooooooooooooooong",
                "me.nya_n.notificationnotifier"
            ),
        )
        AppTheme {
            AppList(
                items = items,
                onAppSelected = { }
            )
        }
    }

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun EmptyAppListComponentScreenshotTest() {
        AppTheme {
            AppList(
                items = emptyList(),
                onAppSelected = { }
            )
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun AppOutlinedButtonComponentScreenshotTest() {
        AppTheme {
            AppOutlinedButton("text") { }
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun AppScaffoldComponentScreenshotTest() {
        val snackbarHostState = remember { SnackbarHostState() }
        AppTheme {
            AppScaffold(snackbarHostState) { }
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun CategoryComponentScreenshotTest() {
        AppTheme {
            Category("カテゴリ")
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun CommonDialogComponentScreenshotTest() {
        AppTheme {
            CommonDialog(
                message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                positiveButtonText = "YES",
                negativeButtonText = "NO",
                onPositiveDismissRequest = { },
                onNegativeDismissRequest = { }
            )
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun RequireNotificationPermissionDialogComponentScreenshotTest() {
        AppTheme {
            RequireNotificationPermissionDialog(
                onDismissRequest = { }
            )
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun RequirePackageVisibilityDialogComponentScreenshotTest() {
        AppTheme {
            RequirePackageVisibilityDialog(
                onDismissRequest = { }
            )
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun EmptyViewComponentScreenshotTest() {
        AppTheme {
            EmptyView(message = "empty")
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun TopBarComponentScreenshotTest() {
        AppTheme {
            TopBar()
        }
    }

    @PreviewTest
    @Preview
    @Composable
    fun SubContentTopBarComponentScreenshotTest() {
        AppTheme {
            TopBar(
                onBack = { }
            )
        }
    }

    // endregion
}