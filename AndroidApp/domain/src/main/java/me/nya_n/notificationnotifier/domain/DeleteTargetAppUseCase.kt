package me.nya_n.notificationnotifier.domain

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.model.InstalledApp

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