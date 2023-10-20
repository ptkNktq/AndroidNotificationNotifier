package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.CheckPackageVisibilityUseCase

class CheckPackageVisibilityUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository
) : CheckPackageVisibilityUseCase {
    override fun invoke(): Boolean {
        return userSettingsRepository.getUserSettings().isPackageVisibilityGranted
    }
}