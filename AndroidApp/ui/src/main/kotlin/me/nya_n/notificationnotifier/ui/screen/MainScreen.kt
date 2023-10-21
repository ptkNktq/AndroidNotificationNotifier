package me.nya_n.notificationnotifier.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppScaffold
import me.nya_n.notificationnotifier.ui.common.EmptyView
import me.nya_n.notificationnotifier.ui.screen.selection.SelectionScreen
import me.nya_n.notificationnotifier.ui.screen.settings.SettingsScreen
import me.nya_n.notificationnotifier.ui.screen.target.TargetScreen
import me.nya_n.notificationnotifier.ui.theme.AppTheme

/** メイン画面 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val activity = LocalContext.current as? Activity
    val scope = rememberCoroutineScope()
    val tabItems = listOf(
        TabItem(stringResource(id = R.string.targets), Icons.Outlined.NotificationsActive) {
            TargetScreen(
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
        TabItem(stringResource(id = R.string.apps), Icons.Rounded.List) {
            SelectionScreen(
                scaffoldState = scaffoldState
            )
        },
        TabItem(stringResource(id = R.string.settings), Icons.Outlined.Settings) {
            SettingsScreen(
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
    )
    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    BackHandler(true) {
        if (pagerState.currentPage == 0) {
            activity?.finish()
        } else {
            scope.launch { pagerState.scrollToPage(0, 0f) }
        }
    }
    MainContent(
        scaffoldState = scaffoldState,
        tabItems = tabItems,
        pagerState = pagerState
    ) {
        scope.launch { pagerState.scrollToPage(it, 0f) }
    }
}

/** メイン画面のコンテンツ本体 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    tabItems: List<TabItem>,
    pagerState: PagerState,
    onTabSelected: (selected: Int) -> Unit
) {
    AppScaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomBar(
                items = tabItems,
                currentPage = pagerState.currentPage,
                onTabSelected = onTabSelected
            )
        }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(it),
            userScrollEnabled = false
        ) { index ->
            Box(modifier = Modifier.fillMaxSize()) {
                tabItems[index].content()
            }
        }
    }
}

@Composable
fun BottomBar(
    items: List<TabItem>,
    currentPage: Int,
    onTabSelected: (selected: Int) -> Unit
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == currentPage,
                onClick = { onTabSelected(index) },
                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                label = { Text(text = item.label) },
            )
        }
    }
}

/** BottomNavigationで表示する各タブの情報 */
data class TabItem(
    val label: String,
    val icon: ImageVector,
    /** このページで表示するコンテンツ
     *   - 初期値として中央に「No Contents...」と表示するViewを定義してある
     */
    val content: @Composable () -> Unit = {
        EmptyView(stringResource(id = R.string.no_contents))
    }
)

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun MainPreview() {
    val tabItems = listOf(
        TabItem("タブ1", Icons.Outlined.NotificationsActive),
        TabItem("タブ2", Icons.Rounded.List),
        TabItem("タブ3", Icons.Outlined.Settings),
    )
    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    AppTheme {
        MainContent(tabItems = tabItems, pagerState = pagerState) { }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    val tabItems = listOf(
        TabItem("タブ1", Icons.Outlined.NotificationsActive),
        TabItem("タブ2", Icons.Rounded.List),
        TabItem("タブ3", Icons.Outlined.Settings),
    )
    AppTheme {
        BottomBar(items = tabItems, currentPage = 0) { }
    }
}