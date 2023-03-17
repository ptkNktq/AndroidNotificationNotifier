package me.nya_n.notificationnotifier.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppScaffold
import me.nya_n.notificationnotifier.ui.common.EmptyView
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
    MainContent(
        tabItems = tabItems,
        pagerState = PagerState(0)
    )
}

/**
 * メイン画面
 */
@Composable
@ExperimentalPagerApi
fun MainScreen(
    navController: NavController,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val activity = LocalContext.current as? Activity
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val tabItems = listOf(
        TabItem(R.string.targets, Icons.Outlined.NotificationsActive) {
            TargetScreen(
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
        TabItem(R.string.apps, Icons.Rounded.List) {
            SelectionScreen(
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
        TabItem(R.string.settings, Icons.Outlined.Settings) {
            SettingScreen(
                navController = navController,
                scaffoldState = scaffoldState
            )
        },
    )
    /* TODO:
     *  サンプルはvarにしてるけどその必要ある？
     *  https://developer.android.com/jetpack/compose/libraries?hl=ja#handling_the_system_back_button
     */
    var backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) {
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
    )
}

@Composable
@ExperimentalPagerApi
fun BottomBar(
    tabItems: List<TabItem>,
    pagerState: PagerState
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
                selected = index == pagerState.currentPage,
                onClick = { scope.launch { pagerState.scrollToPage(index, 0f) } }
            )
        }
    }
}

/**
 * メイン画面のコンテンツ本体
 */
@Composable
@ExperimentalPagerApi
fun MainContent(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    tabItems: List<TabItem>,
    pagerState: PagerState,
) {
    AppScaffold(
        bottomBar = { BottomBar(tabItems = tabItems, pagerState = pagerState) },
        scaffoldState = scaffoldState,
    ) {
        HorizontalPager(
            count = tabItems.size,
            state = pagerState,
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