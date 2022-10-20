package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.repositories.AppRepository

/**
 * 通知条件を読み込む
 */
class LoadFilterConditionUseCase(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(target: InstalledApp): String {
        val cond = appRepository.getFilterCondition(target.packageName)
        return cond?.condition ?: ""
    }
}