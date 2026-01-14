package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.model.InstalledApp

interface LoadAppUseCase {
    suspend operator fun invoke(): Result<Outputs>

    data class Outputs(
        val notTargets: List<InstalledApp>,
        val targets: List<InstalledApp>,
    )
}