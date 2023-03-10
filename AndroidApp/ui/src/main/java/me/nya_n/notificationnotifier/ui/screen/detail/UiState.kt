package me.nya_n.notificationnotifier.ui.screen.detail

import me.nya_n.notificationnotifier.model.Message

data class UiState(
    val condition: String = "",
    val message: Message? = null
)