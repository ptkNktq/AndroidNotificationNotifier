package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.ToggleIgnoreSummaryUseCase
import me.nya_n.notificationnotifier.domain.usecase.ToggleIgnoreSummaryUseCase.Args
import me.nya_n.notificationnotifier.model.FilterCondition

class ToggleIgnoreSummaryUseCaseImpl(
    private val appRepository: AppRepository
) : ToggleIgnoreSummaryUseCase {
    override suspend fun invoke(args: Args): Boolean {
        val target = args.target.packageName
        val data = appRepository.getFilterCondition(target)
        val result = data?.isIgnoreSummary != false
        appRepository.saveFilterCondition(FilterCondition(
            targetPackageName = target,
            isIgnoreSummary = result,
            condition = data?.condition ?: ""
        ))
        return result
    }
}