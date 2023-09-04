package me.nya_n.notificationnotifier.data.repository.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.source.UserSettingsDataStore
import me.nya_n.notificationnotifier.model.UserSettings

class UserSettingsRepositoryImpl(
    private val source: UserSettingsDataStore
) : UserSettingsRepository {
    override fun getUserSettings(): UserSettings {
        return source.get()
    }

    override fun saveUserSettings(setting: UserSettings) {
        source.save(setting)
    }
}