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
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.entity.Scm
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.screen.detail.DetailContent
import me.nya_n.notificationnotifier.ui.screen.license.LicenseContent
import me.nya_n.notificationnotifier.ui.screen.main.MainContent
import me.nya_n.notificationnotifier.ui.screen.main.TabItem
import me.nya_n.notificationnotifier.ui.screen.selection.SelectionContent
import me.nya_n.notificationnotifier.ui.screen.settings.SettingsContent
import me.nya_n.notificationnotifier.ui.screen.target.TargetContent
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import me.nya_n.notificationnotifier.ui.util.Sample

class ContentScreenshotTest {

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
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

    @PreviewTest
    @Preview(backgroundColor = 0xFFC7B5A8, showBackground = true)
    @Composable
    fun LicenseContentScreenshotTest() {
        val snackbarHostState = remember { SnackbarHostState() }
        val libs = Libs(
            libraries = listOf(
                Library(
                    uniqueId = "sample1",
                    artifactVersion = "1.2.3",
                    name = "sample1",
                    description = "test description",
                    website = "https://nya-n.me",
                    developers = listOf(Developer("kani", null)),
                    organization = null,
                    scm = Scm(null, null, null),
                    licenses = setOf(
                        License(
                            name = "Apache-2.0",
                            url = "https://www.apache.org/licenses/LICENSE-2.0",
                            hash = "abc123hash"
                        )
                    )
                ),
                Library(
                    uniqueId = "sample2",
                    artifactVersion = null,
                    name = "sample2",
                    description = null,
                    website = null,
                    developers = listOf(),
                    organization = null,
                    scm = Scm(null, null, null),
                    licenses = setOf(
                        License(
                            name = "MIT",
                            url = "https://opensource.org/licenses/MIT",
                            hash = "def456hash"
                        )
                    ),
                ),
                Library(
                    uniqueId = "sample3",
                    artifactVersion = null,
                    name = "sample3",
                    description = null,
                    website = null,
                    developers = listOf(),
                    organization = null,
                    scm = null,
                    licenses = setOf()
                )
            ),
            licenses = setOf(),
        )
        AppTheme {
            LicenseContent(
                snackbarHostState = snackbarHostState,
                libraries = libs,
                onBack = { }
            )
        }
    }
}