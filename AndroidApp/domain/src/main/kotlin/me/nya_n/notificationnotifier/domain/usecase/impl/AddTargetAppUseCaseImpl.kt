package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.AddTargetAppUseCase
import me.nya_n.notificationnotifier.model.InstalledApp

class AddTargetAppUseCaseImpl(
    private val appRepository: AppRepository
) : AddTargetAppUseCase {
    override suspend operator fun invoke(target: InstalledApp) {
        appRepository.addTargetApp(target)
    }
}
