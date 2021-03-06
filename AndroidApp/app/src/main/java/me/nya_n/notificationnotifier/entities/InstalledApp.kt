package me.nya_n.notificationnotifier.entities

import android.graphics.drawable.Drawable
import java.util.*

data class InstalledApp(
    val label: String,
    val packageName: String,
    val icon: Drawable
) {
    val lowerLabel by lazy { label.toLowerCase(Locale.getDefault()) }
}