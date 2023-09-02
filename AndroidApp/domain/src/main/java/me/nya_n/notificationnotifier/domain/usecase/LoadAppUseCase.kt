package me.nya_n.notificationnotifier.domain.usecase

import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingRepository
import me.nya_n.notificationnotifier.model.AppException
import me.nya_n.notificationnotifier.model.InstalledApp

class LoadAppUseCase(
    private val userSettingRepository: UserSettingRepository,
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(pm: PackageManager): Result<Outputs> = withContext(Dispatchers.IO) {
        val apps = loadInstalledAppList(pm).getOrElse {
            return@withContext Result.failure(it)
        }
        val targets = loadTargetList()
        return@withContext Result.success(Outputs.create(apps, targets))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun loadInstalledAppList(pm: PackageManager): Result<List<InstalledApp>> {
        val setting = userSettingRepository.getUserSetting()
        return if (!setting.isPackageVisibilityGranted) {
            Result.failure(AppException.PermissionDeniedException())
        } else {
            Result.success(appRepository.loadInstalledAppList(pm))
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun loadTargetList(): List<InstalledApp> {
        return appRepository.getTargetAppList()
    }

    data class Outputs(
        val notTargets: List<InstalledApp>,
        val targets: List<InstalledApp>,
    ) {
        companion object {
            fun create(apps: List<InstalledApp>, targets: List<InstalledApp>): Outputs {
                /*
                 * アプリのラベルは度々変更されるがパッケージ名はそう簡単には変更されないので、
                 * パッケージ名だけで判定する
                 */
                val appPackageNames = apps.map { it.packageName }
                val targetPackageNames = targets.map { it.packageName }
                return Outputs(
                    // ターゲットに追加されたアプリは排除
                    apps.filterNot { targetPackageNames.contains(it.packageName) },
                    // アンインストールされたアプリは排除
                    targets.filter { appPackageNames.contains(it.packageName) }
                )
            }
        }
    }
}