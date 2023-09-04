package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp

/**
 * 通知条件を読み込む
 */
interface LoadFilterConditionUseCase {
    suspend operator fun invoke(target: InstalledApp): String
}