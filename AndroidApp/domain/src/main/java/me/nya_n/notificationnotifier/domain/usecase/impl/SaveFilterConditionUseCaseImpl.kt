package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase.Args
import me.nya_n.notificationnotifier.model.FilterCondition

class SaveFilterConditionUseCaseImpl(
    private val appRepository: AppRepository
) : SaveFilterConditionUseCase {
    override suspend operator fun invoke(args: Args) {
        appRepository.saveFilterCondition(
            FilterCondition(
                args.target.packageName,
                args.condition ?: ""
            )
        )
    }
}