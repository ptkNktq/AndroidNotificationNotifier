package me.nya_n.notificationnotifier.domain.usecase

import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import me.nya_n.notificationnotifier.domain.entities.AppException
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository

class LoadAppUseCase(
    private val userSettingRepository: UserSettingRepository,
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(pm: PackageManager): Result<Outputs> {
        val apps = loadInstalledAppList(pm).getOrElse {
            return Result.failure(it)
        }
        val targets = loadTargetList()
        return Result.success(Outputs(apps, targets))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun loadInstalledAppList(pm: PackageManager): Result<List<InstalledApp>> {
        val setting = userSettingRepository.getUserSetting()
        return if (!setting.isPackageVisibilityGranted) {
            Result.failure(AppException.PermissionDeniedException())
        } else {
            Result.success(appRepository.loadInstalledAppList(pm))
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    suspend fun loadTargetList(): List<InstalledApp> {
        return appRepository.getTargetAppList()
    }

    data class Outputs(
        val installs: List<InstalledApp>,
        val targets: List<InstalledApp>,
    )
}