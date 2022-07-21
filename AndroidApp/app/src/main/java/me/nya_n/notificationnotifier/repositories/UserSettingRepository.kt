package me.nya_n.notificationnotifier.repositories

import android.content.Context
import me.nya_n.notificationnotifier.domain.entities.UserSetting
import me.nya_n.notificationnotifier.repositories.sources.UserSettingDataStore

class UserSettingRepository(
    context: Context
) {
    private val source by lazy { UserSettingDataStore(context) }

    fun getUserSetting(): UserSetting {
        return source.get()
    }

    fun saveUserSetting(setting: UserSetting) {
        source.save(setting)
    }
}