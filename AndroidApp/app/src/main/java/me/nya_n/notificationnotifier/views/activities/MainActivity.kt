package me.nya_n.notificationnotifier.views.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.ActivityMainBinding
import me.nya_n.notificationnotifier.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        observes()
        permissionCheck()
    }

    private fun observes() {
        model.fab.observe(this) {
            val fab = it.getContentIfNotHandled() ?: return@observe
            binding.fab.apply {
                setOnClickListener {
                    fab.action.invoke(this)
                }
                if (fab.isShowing) {
                    show()
                } else {
                    hide()
                }
            }
        }
    }

    private fun permissionCheck() {
        if (NotificationManagerCompat.getEnabledListenerPackages(this)
                .contains(packageName)
        ) {
            return
        }
        AlertDialog.Builder(this)
            .setMessage(R.string.require_permission)
            .setPositiveButton(R.string.next) { _, _ ->
                Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                    startActivity(this)
                }
            }.show()
    }
}