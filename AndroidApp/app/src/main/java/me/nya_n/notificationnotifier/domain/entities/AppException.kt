package me.nya_n.notificationnotifier.domain.entities

import androidx.annotation.StringRes
import me.nya_n.notificationnotifier.R

sealed class AppException : Exception() {
    /**
     * エラーテキストのリソースID
     */
    @get:StringRes
    abstract val errorTextResourceId: Int

    class InvalidAddrException : AppException() {
        override val errorTextResourceId: Int
            get() = R.string.validation_error_addr
    }

    class PermissionDeniedException : AppException() {
        override val errorTextResourceId: Int
            get() = R.string.permission_denied
    }
}