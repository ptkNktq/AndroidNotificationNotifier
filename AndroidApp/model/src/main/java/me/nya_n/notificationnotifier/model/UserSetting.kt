package me.nya_n.notificationnotifier.model

import com.google.gson.annotations.SerializedName

data class UserSetting(
    val host: String,
    val port: Int,
    val ssid: String,
    /**
     * @see <a href="https://support.google.com/googleplay/android-developer/answer/10158779?hl=ja">https://support.google.com/googleplay/android-developer/answer/10158779</a>
     */
    @SerializedName("is_package_visibility_granted")
    val isPackageVisibilityGranted: Boolean
)