package me.nya_n.notificationnotifier.domain.usecase

import android.content.pm.PackageManager
import me.nya_n.notificationnotifier.domain.entities.AppException
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository

class LoadAppUseCase(
    private val userSettingRepository: UserSettingRepository,
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(pm: PackageManager): Result<Outputs> {
        val setting = userSettingRepository.getUserSetting()
        if (!setting.isPackageVisibilityGranted) {
            return Result.failure(AppException.PermissionDeniedException())
        }
        val apps = appRepository.loadInstalledAppList(pm)
        val targets = appRepository.getTargetAppList()
        return Result.success(Outputs(apps, targets))
    }

    data class Outputs(
        val installs: List<InstalledApp>,
        val targets: List<InstalledApp>,
    )
}