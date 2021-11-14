package me.nya_n.notificationnotifier.views.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.*
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.ActivityMainBinding
import me.nya_n.notificationnotifier.entities.Backup
import me.nya_n.notificationnotifier.entities.Message
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.viewmodels.MainViewModel
import me.nya_n.notificationnotifier.views.dialogs.DialogListener
import me.nya_n.notificationnotifier.views.dialogs.NotificationAccessPermissionDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity(), DialogListener {
    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModel()

    private val exportDataResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_CANCELED) return@registerForActivityResult
        val uri = it.data?.data
        if (uri != null) {
            model.exportData(this, uri)
        } else {
            handleMessage(Message.Error(R.string.export_failed))
        }
    }

    private val importDataResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_CANCELED) return@registerForActivityResult
        val uri = it.data?.data
        if (uri != null) {
            model.importData(this, uri)
        } else {
            handleMessage(Message.Error(R.string.import_failed))
        }
    }

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
                exportDataResult.launch(
                    Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/json"
                        putExtra(Intent.EXTRA_TITLE, Backup.FILE_NAME)
                    }
                )
                true
            }
            R.id.import_data -> {
                importDataResult.launch(
                    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/json"
                    }
                )
                true
            }
            R.id.licence -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPositiveClick(dialog: DialogFragment) {
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
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
        model.message.observe(this) {
            val message = it.getContentIfNotHandled() ?: return@observe
            handleMessage(message)
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

    private fun handleMessage(message: Message) {
        Snackbar.create(binding.root, message).show()
    }
}