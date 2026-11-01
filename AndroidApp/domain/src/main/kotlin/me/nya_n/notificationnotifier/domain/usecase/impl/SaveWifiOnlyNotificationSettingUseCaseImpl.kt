package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.SaveWifiOnlyNotificationSettingUseCase

class SaveWifiOnlyNotificationSettingUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository
) : SaveWifiOnlyNotificationSettingUseCase {
    override fun invoke(isWifiOnlyNotificationEnabled: Boolean): Result<Unit> {
        return runCatching {
            val settings = userSettingsRepository.getUserSettings()
            userSettingsRepository.saveUserSettings(
                settings.copy(
                    isWifiOnlyNotificationEnabled = isWifiOnlyNotificationEnabled
                )
            )
            return Result.success(Unit)
        }
    }
}