package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp

/** 通知対象から外す */
interface DeleteTargetAppUseCase {
    suspend operator fun invoke(target: InstalledApp)
}