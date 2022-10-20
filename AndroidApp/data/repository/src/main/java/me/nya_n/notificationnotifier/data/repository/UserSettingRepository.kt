package me.nya_n.notificationnotifier.data.repository

import me.nya_n.notificationnotifier.data.repository.source.UserSettingDataStore
import me.nya_n.notificationnotifier.model.UserSetting

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