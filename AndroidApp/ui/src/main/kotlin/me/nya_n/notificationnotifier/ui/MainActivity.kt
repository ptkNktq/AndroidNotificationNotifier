package me.nya_n.notificationnotifier.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.ui.dialogs.NotificationAccessPermissionDialog
import me.nya_n.notificationnotifier.ui.screen.app.AppScreen
import me.nya_n.notificationnotifier.ui.theme.AppColors

class MainActivity : AppCompatActivity() {
    private val isReady = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { !isReady.value }
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(
                    AppColors.Primary.toArgb(),
                ),
                navigationBarStyle = SystemBarStyle.dark(
                    AppColors.Primary.toArgb()
                )
            )
            AppScreen()
        }
        setUpPermissionsCheckResultListener()
        lifecycleScope.launch {
            delay(500)
            isReady.update { true }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionsCheck()
    }

    private fun permissionsCheck() {
        if (!NotificationManagerCompat.getEnabledListenerPackages(this)
                .contains(packageName)
        ) {
            NotificationAccessPermissionDialog.showOnlyOnce(supportFragmentManager)
        }
    }

    private fun setUpPermissionsCheckResultListener() {
        supportFragmentManager.setFragmentResultListener(
            NotificationAccessPermissionDialog.TAG, this
        ) { _, res ->
            if (res.getBoolean(NotificationAccessPermissionDialog.KEY_IS_NEXT)) {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            } else {
                finish()
            }
        }
    }
}