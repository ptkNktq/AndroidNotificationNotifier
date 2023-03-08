package me.nya_n.notificationnotifier.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.EmptyView
import me.nya_n.notificationnotifier.ui.common.TopBar
import me.nya_n.notificationnotifier.ui.screen.selection.SelectionScreen
import me.nya_n.notificationnotifier.ui.screen.setting.SettingScreen
import me.nya_n.notificationnotifier.ui.screen.target.TargetScreen
import me.nya_n.notificationnotifier.ui.theme.AppColors

@Composable
@Preview
@ExperimentalPagerApi
fun MainPreview() {
    val tabItems = listOf(
        TabItem(R.string.targets, Icons.Outlined.NotificationsActive),
        TabItem(R.string.apps, Icons.Rounded.List),
        TabItem(R.string.settings, Icons.Outlined.Settings),
    )
    MainContent(tabItems, 2)
}

/**
 * メイン画面
 */
@Composable
@ExperimentalPagerApi
fun MainScreen(
    navController: NavController,
) {
    val tabItems = listOf(
        TabItem(R.string.targets, Icons.Outlined.NotificationsActive) { TargetScreen() },
        TabItem(R.string.apps, Icons.Rounded.List) { SelectionScreen() },
        TabItem(R.string.settings, Icons.Outlined.Settings) { SettingScreen(navController) },
    )
    MainContent(tabItems)
}

@Composable
@ExperimentalPagerApi
fun BottomBar(
    tabItems: List<TabItem>,
    state: PagerState
) {
    val scope = rememberCoroutineScope()
    BottomNavigation(
        backgroundColor = AppColors.Brown
    ) {
        tabItems.forEachIndexed { index, tabItem ->
            BottomNavigationItem(
                label = { Text(text = stringResource(id = tabItem.labelResourceId)) },
                icon = { Icon(imageVector = tabItem.icon, contentDescription = null) },
                selectedContentColor = Color.White,
                selected = index == state.currentPage,
                onClick = { scope.launch { state.scrollToPage(index, 0f) } }
            )
        }
    }
}

@Composable
@ExperimentalPagerApi
fun MainContent(
    tabItems: List<TabItem>,
    initPage: Int = 0
) {
    val state = rememberPagerState(initPage)
    Scaffold(
        backgroundColor = AppColors.RoseBrown,
        topBar = { TopBar() },
        bottomBar = { BottomBar(tabItems = tabItems, state = state) },
        modifier = Modifier.systemBarsPadding()
    ) {
        HorizontalPager(
            count = tabItems.size,
            state = state,
            userScrollEnabled = false,
            modifier = Modifier.padding(it),
        ) { index ->
            Box(modifier = Modifier.fillMaxSize()) {
                tabItems[index].content()
            }
        }
    }
}

/**
 * BottomNavigationで表示する各タブの情報
 */
data class TabItem(
    @StringRes
    val labelResourceId: Int,
    val icon: ImageVector,
    /**
     * このページで表示するコンテンツ
     *  - 初期値として中央に「No Contents...」と表示するViewを定義してある
     */
    val content: @Composable () -> Unit = { EmptyView(textResourceId = R.string.no_contents) }
)