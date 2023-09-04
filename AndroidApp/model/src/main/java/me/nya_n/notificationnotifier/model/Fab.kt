package me.nya_n.notificationnotifier.model

import com.google.android.material.floatingactionbutton.FloatingActionButton

data class Fab(
    val isShowing: Boolean,
    val action: (view: FloatingActionButton) -> Unit = {}
)