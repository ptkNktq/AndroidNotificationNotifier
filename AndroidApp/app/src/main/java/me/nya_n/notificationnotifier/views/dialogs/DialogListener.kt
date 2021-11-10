package me.nya_n.notificationnotifier.views.dialogs

import androidx.fragment.app.DialogFragment

interface DialogListener {
    fun onPositiveClick(dialog: DialogFragment)
    fun onNegativeClick(dialog: DialogFragment)
}