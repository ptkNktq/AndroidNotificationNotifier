package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.GetUserSettingsUseCase
import me.nya_n.notificationnotifier.model.UserSettings

class GetUserSettingsUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository
) : GetUserSettingsUseCase {
    override fun invoke(): UserSettings {
        return userSettingsRepository.getUserSettings()
    }
}