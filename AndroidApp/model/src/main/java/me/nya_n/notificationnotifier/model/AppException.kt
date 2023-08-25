package me.nya_n.notificationnotifier.model

sealed class AppException : Exception() {
    /** アドレスの形式が不正 */
    class InvalidAddrException : AppException()

    /** 権限がない */
    class PermissionDeniedException : AppException()
}