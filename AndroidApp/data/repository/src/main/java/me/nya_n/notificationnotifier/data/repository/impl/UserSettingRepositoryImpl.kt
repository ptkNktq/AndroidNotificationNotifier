package me.nya_n.notificationnotifier.data.repository.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingRepository
import me.nya_n.notificationnotifier.data.repository.source.UserSettingDataStore
import me.nya_n.notificationnotifier.model.UserSetting

class UserSettingRepositoryImpl(
    private val source: UserSettingDataStore
) : UserSettingRepository {
    override fun getUserSetting(): UserSetting {
        return source.get()
    }

    override fun saveUserSetting(setting: UserSetting) {
        source.save(setting)
    }
}