package me.nya_n.notificationnotifier.domain.usecase.impl

import android.Manifest
import android.app.Notification
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.NotifyTargetAppNotificationUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase

class NotifyTargetAppNotificationUseCaseImpl(
    private val connectionManager: ConnectivityManager,
    private val appRepository: AppRepository,
    private val userSettingsRepository: UserSettingsRepository,
    private val notifyUseCase: NotifyUseCase,
) : NotifyTargetAppNotificationUseCase {
    @get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private val isConnectedToWifi: Boolean
        get() {
            val network = connectionManager.activeNetwork ?: return false
            val capabilities = connectionManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }

    override suspend operator fun invoke(
        packageName: String,
        title: String,
        message: String,
        flags: Int
    ): Result<Unit> {
        return runCatching {
            val settings = userSettingsRepository.getUserSettings()
            if (settings.isWifiOnlyNotificationEnabled && !isConnectedToWifi) {
                return Result.success(Unit)
            }

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
            notifyUseCase("${title}\n${message}")
        }
    }
}