package me.nya_n.notificationnotifier.ui.screen.selection

import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.model.Message

data class UiState(
    /**
     * インストール済みアプリ一覧
     */
    val items: List<InstalledApp> = emptyList(),
    val message: Message? = null
)