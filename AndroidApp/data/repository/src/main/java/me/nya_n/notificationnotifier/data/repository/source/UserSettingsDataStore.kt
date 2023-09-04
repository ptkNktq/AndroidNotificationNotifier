package me.nya_n.notificationnotifier.data.repository.source

import android.content.SharedPreferences
import me.nya_n.notificationnotifier.model.UserSettings

class UserSettingsDataStore(
    pref: SharedPreferences
) : KeyValueDataStore(pref) {

    fun get(): UserSettings {
        return UserSettings(
            get(KEY_HOST, DEFAULT_HOST),
            get(KEY_PORT, DEFAULT_PORT),
            get(KEY_IS_PACKAGE_VISIBILITY_GRANTED, DEFAULT_IS_PACKAGE_VISIBILITY_GRANTED)
        )
    }

    fun save(settings: UserSettings) {
        put(KEY_HOST, settings.host)
        put(KEY_PORT, settings.port)
        put(KEY_IS_PACKAGE_VISIBILITY_GRANTED, settings.isPackageVisibilityGranted)
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