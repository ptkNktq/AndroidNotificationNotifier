package me.nya_n.notificationnotifier.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.common.AppScaffold
import me.nya_n.notificationnotifier.ui.common.EmptyView
import me.nya_n.notificationnotifier.ui.screen.app.Screen
import me.nya_n.notificationnotifier.ui.screen.detail.DetailScreen
import me.nya_n.notificationnotifier.ui.screen.selection.SelectionScreen
import me.nya_n.notificationnotifier.ui.screen.settings.SettingsScreen
import me.nya_n.notificationnotifier.ui.screen.target.TargetScreen
import me.nya_n.notificationnotifier.ui.theme.AppTheme
import me.nya_n.notificationnotifier.ui.util.LocalAnimatedVisibilityScope

/** メイン画面 */
@Composable
fun MainScreen(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    var onBack by remember { mutableStateOf<(() -> Unit)?>(null) }
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    val tabItems = listOf(
        TabItem(stringResource(id = R.string.targets), Icons.Outlined.NotificationsActive) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Main.Targets.name
            ) {
                composable(Screen.Main.Targets.route) {
                    CompositionLocalProvider(
                        LocalAnimatedVisibilityScope provides this@composable
                    ) {
                        onBack = null
                        TargetScreen(
                            navController = navController,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
                composable(Screen.Main.Detail.route) {
                    val app = Gson().fromJson(
                        it.arguments?.getString("app"),
                        InstalledApp::class.java
                    )
                    CompositionLocalProvider(
                        LocalAnimatedVisibilityScope provides this@composable
                    ) {
                        onBack = { navController.popBackStack() }
                        DetailScreen(
                            navController = navController,
                            app = app
                        )
                    }
                }
            }
        },
        TabItem(stringResource(id = R.string.apps), Icons.AutoMirrored.Rounded.List) {
            onBack = null
            SelectionScreen(snackbarHostState = snackbarHostState)
        },
        TabItem(stringResource(id = R.string.settings), Icons.Outlined.Settings) {
            onBack = null
            SettingsScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        },
    )
    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    BackHandler(true) {
        snackbarHostState.currentSnackbarData?.dismiss()
        if (pagerState.currentPage == 0) {
            activity?.finish()
        } else {
            scope.launch { pagerState.scrollToPage(0, 0f) }
        }
    }
    MainContent(
        snackbarHostState = snackbarHostState,
        tabItems = tabItems,
        pagerState = pagerState,
        onBack = onBack,
        onTabSelected = {
            snackbarHostState.currentSnackbarData?.dismiss()
            scope.launch { pagerState.scrollToPage(it, 0f) }
        },

        )
}

/** メイン画面のコンテンツ本体 */
@Composable
fun MainContent(
    snackbarHostState: SnackbarHostState,
    tabItems: List<TabItem>,
    pagerState: PagerState,
    onBack: (() -> Unit)? = null,
    onTabSelected: (selected: Int) -> Unit,
) {
    AppScaffold(
        snackbarHostState = snackbarHostState,
        onBack = onBack,
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
private fun BottomBar(
    items: List<TabItem>,
    currentPage: Int,
    onTabSelected: (selected: Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
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
        EmptyView(message = stringResource(id = R.string.no_contents))
    }
)

@Preview
@Composable
private fun MainPreview() {
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
            pagerState = pagerState,
            onTabSelected = { },
            onBack = null
        )
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    val tabItems = listOf(
        TabItem("タブ1", Icons.Outlined.NotificationsActive),
        TabItem("タブ2", Icons.AutoMirrored.Rounded.List),
        TabItem("タブ3", Icons.Outlined.Settings),
    )
    AppTheme {
        BottomBar(items = tabItems, currentPage = 0) { }
    }
}