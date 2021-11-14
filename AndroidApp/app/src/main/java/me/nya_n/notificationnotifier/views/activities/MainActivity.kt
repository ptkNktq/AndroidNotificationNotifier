package me.nya_n.notificationnotifier.views.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.ActivityMainBinding
import me.nya_n.notificationnotifier.viewmodels.MainViewModel
import me.nya_n.notificationnotifier.views.dialogs.DialogListener
import me.nya_n.notificationnotifier.views.dialogs.NotificationAccessPermissionDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), DialogListener {
    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        observes()
    }

    override fun onResume() {
        super.onResume()
        permissionCheck()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.export_data -> {
                true
            }
            R.id.import_data -> {
                true
            }
            R.id.licence -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPositiveClick(dialog: DialogFragment) {
        Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
            startActivity(this)
        }
    }

    override fun onNegativeClick(dialog: DialogFragment) {
        finish()
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
        NotificationAccessPermissionDialog.showOnlyOnce(supportFragmentManager)
    }
}