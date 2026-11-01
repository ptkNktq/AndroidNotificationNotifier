package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp

class LoadFilterConditionUseCaseImpl(
    private val appRepository: AppRepository
) : LoadFilterConditionUseCase {
    override suspend operator fun invoke(target: InstalledApp): FilterCondition {
        return appRepository.getFilterConditionOrDefault(target.packageName)
    }
}