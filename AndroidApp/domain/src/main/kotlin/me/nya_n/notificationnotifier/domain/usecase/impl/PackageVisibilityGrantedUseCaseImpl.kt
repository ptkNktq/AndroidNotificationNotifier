package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.PackageVisibilityGrantedUseCase

class PackageVisibilityGrantedUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository
) : PackageVisibilityGrantedUseCase {
    override operator fun invoke() {
        val settings = userSettingsRepository.getUserSettings()
            .copy(isPackageVisibilityGranted = true)
        userSettingsRepository.saveUserSettings(settings)
    }
}