package me.nya_n.notificationnotifier.ui.util

import me.nya_n.notificationnotifier.model.InstalledApp

object Sample {
    val items: List<InstalledApp>
        get() {
            return (0..10).map {
                InstalledApp("Sample App ${it + 1}", "me.nya_n.notificationnotifier")
            }
        }
}