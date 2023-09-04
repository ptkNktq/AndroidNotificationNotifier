package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.model.InstalledApp

class LoadFilterConditionUseCaseImpl(
    private val appRepository: AppRepository
) : LoadFilterConditionUseCase {
    override suspend operator fun invoke(target: InstalledApp): String {
        val cond = appRepository.getFilterCondition(target.packageName)
        return cond?.condition ?: ""
    }
}