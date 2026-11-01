package me.nya_n.notificationnotifier.domain.usecase

interface SaveWifiOnlyNotificationSettingUseCase {
    operator fun invoke(isWifiOnlyNotificationEnabled: Boolean): Result<Unit>
}