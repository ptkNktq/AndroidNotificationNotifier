package me.nya_n.notificationnotifier.repositories

import me.nya_n.notificationnotifier.domain.entities.UserSetting
import me.nya_n.notificationnotifier.repositories.sources.UserSettingDataStore

class UserSettingRepository(
    private val source: UserSettingDataStore
) {
    fun getUserSetting(): UserSetting {
        return source.get()
    }

    fun saveUserSetting(setting: UserSetting) {
        source.save(setting)
    }
}