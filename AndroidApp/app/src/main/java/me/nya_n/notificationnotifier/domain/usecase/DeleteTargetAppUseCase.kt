package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.repositories.AppRepository

/**
 * 通知対象から外す
 */
class DeleteTargetAppUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(target: InstalledApp) {
        appRepository.deleteTargetApp(target)
    }
}