package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.DeleteTargetAppUseCase
import me.nya_n.notificationnotifier.model.InstalledApp

class DeleteTargetAppUseCaseImpl(
    private val appRepository: AppRepository
) : DeleteTargetAppUseCase {
    override suspend operator fun invoke(target: InstalledApp) {
        appRepository.deleteTargetApp(target)
    }
}