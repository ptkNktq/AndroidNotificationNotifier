package me.nya_n.notificationnotifier.ui.screen.settings

import me.nya_n.notificationnotifier.model.Message

data class UiState(
    val address: String = "",
    val message: Message? = null
)