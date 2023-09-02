package me.nya_n.notificationnotifier.domain.usecase

interface NotifyTargetAppNotificationUseCase {
    suspend operator fun invoke(
        packageName: String,
        title: String,
        message: String
    ): Result<Unit>
}