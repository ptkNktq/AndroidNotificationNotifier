package me.nya_n.notificationnotifier.model

data class UserSetting(
    val host: String,
    val port: Int,
    /**
     * @see <a href="https://support.google.com/googleplay/android-developer/answer/10158779?hl=ja">https://support.google.com/googleplay/android-developer/answer/10158779</a>
     */
    val isPackageVisibilityGranted: Boolean
)