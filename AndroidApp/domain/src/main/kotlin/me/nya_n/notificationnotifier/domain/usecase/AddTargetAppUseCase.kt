package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp

/**
 * 通知対象に追加
 */
interface AddTargetAppUseCase {
    suspend operator fun invoke(target: InstalledApp)
}