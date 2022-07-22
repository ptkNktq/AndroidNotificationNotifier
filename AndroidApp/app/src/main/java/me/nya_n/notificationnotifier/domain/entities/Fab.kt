package me.nya_n.notificationnotifier.domain.entities

import com.google.android.material.floatingactionbutton.FloatingActionButton

data class Fab(
    val isShowing: Boolean,
    val action: (view: FloatingActionButton) -> Unit = {}
)