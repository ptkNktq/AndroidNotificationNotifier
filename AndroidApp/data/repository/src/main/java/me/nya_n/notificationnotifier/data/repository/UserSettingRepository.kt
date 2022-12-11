package me.nya_n.notificationnotifier.data.repository

import me.nya_n.notificationnotifier.model.UserSetting

interface UserSettingRepository {
    fun getUserSetting(): UserSetting

    fun saveUserSetting(setting: UserSetting)
}