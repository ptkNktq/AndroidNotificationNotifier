package me.nya_n.notificationnotifier.data.repository

import me.nya_n.notificationnotifier.model.UserSettings

interface UserSettingsRepository {
    fun getUserSettings(): UserSettings

    fun saveUserSettings(setting: UserSettings)
}