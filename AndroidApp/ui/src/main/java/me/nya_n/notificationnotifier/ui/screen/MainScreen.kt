package me.nya_n.notificationnotifier.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppColor
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
@Preview
@ExperimentalPagerApi
fun MainScreenPreview() {
    MainScreen()
}

/**
 * メイン画面
 * @param initPage 最初に表示するページ番号(デバッグ/テスト用)
 */
@Composable
@ExperimentalPagerApi
fun MainScreen(
    initPage: Int = 0
) {
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(initPage)
    val tabItems = listOf(
        TabItem(R.string.targets, Icons.Outlined.NotificationsActive),
        TabItem(R.string.apps, Icons.Rounded.List),
        TabItem(R.string.settings, Icons.Outlined.Settings),
    )
    AppTheme {
        Scaffold(
            backgroundColor = AppColor.RoseBrown,
            topBar = {
                TopAppBar(
                    title = { Text(text = "NotificationNotifier", color = Color.White) },
                    backgroundColor = AppColor.Brown
                )
            },
            bottomBar = {
                BottomNavigation(
                    backgroundColor = AppColor.Brown
                ) {
                    tabItems.forEachIndexed { index, tabItem ->
                        BottomNavigationItem(
                            label = { Text(text = stringResource(id = tabItem.labelResourceId)) },
                            icon = { Icon(imageVector = tabItem.icon, contentDescription = null) },
                            selectedContentColor = Color.White,
                            selected = index == pageState.currentPage,
                            onClick = { scope.launch { pageState.scrollToPage(index, 0f) } }
                        )
                    }
                }
            },
        ) {
            HorizontalPager(
                count = tabItems.size,
                state = pageState,
                userScrollEnabled = false,
                modifier = Modifier.padding(it),
            ) { index ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    tabItems[index].content()
                }
            }
        }
    }
}

data class TabItem(
    @StringRes
    val labelResourceId: Int,
    val icon: ImageVector,
    val content: @Composable () -> Unit = { Text(text = "No Contents...") }
)