package me.nya_n.notificationnotifier.views.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import me.nya_n.notificationnotifier.R

class NotificationAccessPermissionDialog : DialogFragment() {
    private lateinit var listener: DialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.require_permission)
                .setPositiveButton(R.string.next) { _, _ ->
                    listener.onPositiveClick(this)
                }
                .setNegativeButton(R.string.ng) { _, _ ->
                    listener.onNegativeClick(this)
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is DialogListener) {
            throw IllegalStateException("$context must implement DialogListener")
        }
        listener = context
    }

    companion object {
        const val TAG = "notification_access_permission"

        fun showOnlyOnce(fm: FragmentManager) {
            val f = fm.findFragmentByTag(TAG)
            if (f != null) return
            NotificationAccessPermissionDialog()
                .show(fm, TAG)
        }
    }
}