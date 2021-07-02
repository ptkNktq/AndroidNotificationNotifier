package me.nya_n.notificationnotifier.repositories.sources

import android.content.Context
import me.nya_n.notificationnotifier.entities.UserSetting

class UserSettingDataStore(
    context: Context
) : KeyValueDataStore(context) {

    fun get(): UserSetting {
        return UserSetting(
            get(KEY_HOST, DEFAULT_HOST),
            get(KEY_PORT, DEFAULT_PORT)
        )
    }

    fun save(setting: UserSetting) {
        put(KEY_HOST, setting.host)
        put(KEY_PORT, setting.port)
    }

    companion object {
        const val KEY_HOST = "host"
        const val DEFAULT_HOST = ""
        const val KEY_PORT = "port"
        const val DEFAULT_PORT = -1
    }
}