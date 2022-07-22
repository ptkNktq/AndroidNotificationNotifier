package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.domain.entities.FilterCondition
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.repositories.AppRepository

/**
 * 通知条件を保存
 */
class SaveFilterConditionUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(args: Args) {
        appRepository.saveFilterCondition(
            FilterCondition(
                args.target.packageName,
                args.condition ?: ""
            )
        )
    }

    data class Args(
        val target: InstalledApp,
        val condition: String?
    )
}