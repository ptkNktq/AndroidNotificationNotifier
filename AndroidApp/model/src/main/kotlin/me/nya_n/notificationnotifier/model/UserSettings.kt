package me.nya_n.notificationnotifier.model

import com.google.gson.annotations.SerializedName

data class UserSettings(
    val host: String,
    val port: Int,
    /**
     * @see <a href="https://support.google.com/googleplay/android-developer/answer/10158779?hl=ja">パッケージ（アプリ）の広範な一覧取得（QUERY_ALL_PACKAGES）権限の使用</a>
     */
    @SerializedName("is_package_visibility_granted")
    val isPackageVisibilityGranted: Boolean,
    @SerializedName("is_wifi_only_notification_enabled")
    val isWifiOnlyNotificationEnabled: Boolean
)