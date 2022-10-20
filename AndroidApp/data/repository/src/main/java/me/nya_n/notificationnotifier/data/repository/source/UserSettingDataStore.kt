package me.nya_n.notificationnotifier.data.repository.source

import android.content.SharedPreferences
import me.nya_n.notificationnotifier.model.UserSetting

class UserSettingDataStore(
    pref: SharedPreferences
) : KeyValueDataStore(pref) {

    fun get(): UserSetting {
        return UserSetting(
            get(KEY_HOST, DEFAULT_HOST),
            get(KEY_PORT, DEFAULT_PORT),
            get(KEY_IS_PACKAGE_VISIBILITY_GRANTED, DEFAULT_IS_PACKAGE_VISIBILITY_GRANTED)
        )
    }

    fun save(setting: UserSetting) {
        put(KEY_HOST, setting.host)
        put(KEY_PORT, setting.port)
        put(KEY_IS_PACKAGE_VISIBILITY_GRANTED, setting.isPackageVisibilityGranted)
    }

    companion object {
        const val DATA_STORE_NAME = "settings"
        const val KEY_HOST = "host"
        const val DEFAULT_HOST = ""
        const val KEY_PORT = "port"
        const val DEFAULT_PORT = -1
        const val KEY_IS_PACKAGE_VISIBILITY_GRANTED = "isPackageVisibilityGranted"
        const val DEFAULT_IS_PACKAGE_VISIBILITY_GRANTED = false
    }
}