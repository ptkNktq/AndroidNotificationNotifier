package me.nya_n.notificationnotifier.domain

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.model.InstalledApp

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