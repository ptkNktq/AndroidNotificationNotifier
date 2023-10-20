package me.nya_n.notificationnotifier.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import me.nya_n.notificationnotifier.ui.R

class NotificationAccessPermissionDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            isCancelable = false
            AlertDialog.Builder(it)
                .setMessage(R.string.require_permission)
                .setPositiveButton(R.string.next) { _, _ ->
                    setFragmentResult(TAG, bundleOf(KEY_IS_NEXT to true))
                }
                .setNegativeButton(R.string.ng) { _, _ ->
                    setFragmentResult(TAG, bundleOf(KEY_IS_NEXT to false))
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "notification_access_permission"
        const val KEY_IS_NEXT = "isNext"

        fun showOnlyOnce(fm: FragmentManager) {
            val f = fm.findFragmentByTag(TAG)
            if (f != null) return
            NotificationAccessPermissionDialog()
                .show(fm, TAG)
        }
    }
}