package me.nya_n.notificationnotifier.entities

import java.io.Serializable
import java.util.*

data class InstalledApp(
    val label: String,
    val packageName: String
) : Serializable {
    val lowerLabel by lazy { label.toLowerCase(Locale.getDefault()) }
}