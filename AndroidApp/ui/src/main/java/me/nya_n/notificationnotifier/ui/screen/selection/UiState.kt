package me.nya_n.notificationnotifier.ui.screen.selection

import me.nya_n.notificationnotifier.model.InstalledApp

data class UiState(
    /**
     * インストール済みアプリ一覧
     */
    val items: List<InstalledApp> = emptyList()
)