package me.nya_n.notificationnotifier.model

import androidx.annotation.StringRes

sealed class Message {
    class Notice(
        @StringRes
        val message: Int,
        vararg val args: Any
    ) : Message()

    class Error(
        @StringRes
        val message: Int
    ) : Message()
}