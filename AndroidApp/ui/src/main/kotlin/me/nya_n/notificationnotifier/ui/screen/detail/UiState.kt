package me.nya_n.notificationnotifier.ui.screen.detail

import me.nya_n.notificationnotifier.model.Message

data class UiState(
    /** サマリー用の通知を無視するか？ */
    val isIgnoreSummary: Boolean = false,

    /** 通知を送信する条件 */
    val condition: String = "",

    /** ユーザーに表示するメッセージ */
    val message: Message? = null
)