package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp

interface ToggleIgnoreSummaryUseCase {
    suspend fun invoke(args: Args): Boolean

    data class Args(val target: InstalledApp)
}