package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp

/**
 * 通知条件を保存
 */
interface SaveFilterConditionUseCase {
    suspend operator fun invoke(args: Args)

    data class Args(
        val target: InstalledApp,
        val condition: String?
    )
}