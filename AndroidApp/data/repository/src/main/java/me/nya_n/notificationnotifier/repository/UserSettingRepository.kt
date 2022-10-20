package me.nya_n.notificationnotifier.repository

import me.nya_n.notificationnotifier.model.UserSetting
import me.nya_n.notificationnotifier.repository.source.UserSettingDataStore

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