package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.UserSettings

interface GetUserSettingsUseCase {
    operator fun invoke(): UserSettings
}