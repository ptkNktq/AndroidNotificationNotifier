package me.nya_n.notificationnotifier.entities

import com.google.gson.annotations.SerializedName

data class UserSetting(
    val host: String,
    val port: Int,
    @SerializedName("is_package_visibility_granted")
    val isPackageVisibilityGranted: Boolean
)