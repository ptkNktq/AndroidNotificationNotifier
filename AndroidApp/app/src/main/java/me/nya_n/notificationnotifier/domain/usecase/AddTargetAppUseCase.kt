package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.repository.AppRepository

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