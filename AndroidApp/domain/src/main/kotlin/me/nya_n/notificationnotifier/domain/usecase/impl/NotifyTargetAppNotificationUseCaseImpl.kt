package me.nya_n.notificationnotifier.domain.usecase.impl

import android.app.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.NotifyTargetAppNotificationUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase

class NotifyTargetAppNotificationUseCaseImpl(
    private val appRepository: AppRepository,
    private val notifyUseCase: NotifyUseCase
) : NotifyTargetAppNotificationUseCase {
    override suspend operator fun invoke(
        packageName: String,
        title: String,
        message: String,
        flags: Int
    ): Result<Unit> {
        return runCatching {
            val targets = appRepository.getTargetAppList()
            if (!targets.any { t -> t.packageName == packageName }) {
                return Result.success(Unit)
            }

            val cond = appRepository.getFilterCondition(packageName)
            if (cond != null) {
                if (cond.condition.isNotEmpty()) {
                    val regex = Regex(pattern = cond.condition)
                    if (!regex.matches("$title $message")) {
                        return Result.success(Unit)
                    }
                }
                val isSummary = flags and Notification.FLAG_GROUP_SUMMARY != 0
                if (cond.isIgnoreSummary && isSummary) {
                    return Result.success(Unit)
                }
            }

            withContext(Dispatchers.IO) {
                notifyUseCase("${title}\n${message}")
            }
        }
    }
}