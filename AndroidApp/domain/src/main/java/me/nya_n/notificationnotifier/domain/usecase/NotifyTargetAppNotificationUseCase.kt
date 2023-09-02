package me.nya_n.notificationnotifier.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.AppRepository

class NotifyTargetAppNotificationUseCase(
    private val appRepository: AppRepository,
    private val notifyUseCase: NotifyUseCase
) {
    suspend operator fun invoke(
        packageName: String,
        title: String,
        message: String
    ): Result<Unit> {
        return runCatching {
            val targets = appRepository.getTargetAppList()
            if (!targets.any { t -> t.packageName == packageName }) {
                return Result.success(Unit)
            }

            val cond = appRepository.getFilterCondition(packageName)
            if (cond != null && cond.condition.isNotEmpty()) {
                val regex = Regex(pattern = cond.condition)
                if (!regex.matches("$title $message")) {
                    return Result.success(Unit)
                }
            }

            withContext(Dispatchers.IO) {
                notifyUseCase("${title}\n${message}")
            }
        }
    }
}