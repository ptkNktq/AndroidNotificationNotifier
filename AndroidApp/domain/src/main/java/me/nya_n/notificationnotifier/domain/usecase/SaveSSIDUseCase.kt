package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.data.repository.UserSettingRepository

/**
 * SSIDを保存する
 */
class SaveSSIDUseCase(
    private val userSettingRepository: UserSettingRepository
) {
    operator fun invoke(ssid: String): Result<Unit> {
        val setting = userSettingRepository.getUserSetting()
            .copy(ssid = ssid)
        userSettingRepository.saveUserSetting(setting)
        return Result.success(Unit)
    }
}