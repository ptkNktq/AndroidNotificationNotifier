package me.nya_n.notificationnotifier.domain.usecase

interface NotifyUseCase {
    suspend operator fun invoke(message: String): Result<Unit>
}