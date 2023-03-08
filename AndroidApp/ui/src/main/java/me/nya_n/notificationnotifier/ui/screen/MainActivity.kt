package me.nya_n.notificationnotifier.ui.screen

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import com.google.accompanist.pager.ExperimentalPagerApi
import me.nya_n.notificationnotifier.ui.dialogs.DialogListener
import me.nya_n.notificationnotifier.ui.dialogs.NotificationAccessPermissionDialog

class MainActivity : AppCompatActivity(), DialogListener {

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        permissionCheck()
    }

    override fun onPositiveClick(dialog: DialogFragment) {
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    override fun onNegativeClick(dialog: DialogFragment) {
        finish()
    }

    private fun permissionCheck() {
        if (NotificationManagerCompat.getEnabledListenerPackages(this)
                .contains(packageName)
        ) {
            return
        }
        NotificationAccessPermissionDialog.showOnlyOnce(supportFragmentManager)
    }
}