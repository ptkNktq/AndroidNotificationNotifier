package me.nya_n.notificationnotifier.domain

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.model.InstalledApp

/**
 * 通知対象に追加
 */
class AddTargetAppUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(target: InstalledApp) {
        appRepository.addTargetApp(target)
    }
}