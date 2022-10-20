package me.nya_n.notificationnotifier.ui.dialogs

import androidx.fragment.app.DialogFragment

interface DialogListener {
    fun onPositiveClick(dialog: DialogFragment)
    fun onNegativeClick(dialog: DialogFragment)
}