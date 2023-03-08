package me.nya_n.notificationnotifier.ui.screen.target

import me.nya_n.notificationnotifier.model.InstalledApp

data class UiState(
    /**
     * 通知送信ターゲットに追加したアプリ一覧
     */
    val items: List<InstalledApp> = emptyList()
)