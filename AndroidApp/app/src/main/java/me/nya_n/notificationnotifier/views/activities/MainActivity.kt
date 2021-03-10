package me.nya_n.notificationnotifier.views.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import me.nya_n.notificationnotifier.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        permissionCheck()
    }

    private fun permissionCheck() {
        if (NotificationManagerCompat.getEnabledListenerPackages(this)
                .contains(packageName)) {
            return
        }
        AlertDialog.Builder(this)
            .setMessage(R.string.require_permission)
            .setPositiveButton(R.string.next) { _, _ ->
                Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                    startActivity(this)
                }
            }
            .show()
    }
}