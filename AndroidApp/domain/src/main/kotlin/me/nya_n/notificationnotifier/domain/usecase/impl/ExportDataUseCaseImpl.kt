package me.nya_n.notificationnotifier.domain.usecase.impl

import android.net.Uri
import com.google.gson.Gson
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.BackupRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.domain.usecase.ExportDataUseCase
import me.nya_n.notificationnotifier.model.Backup

class ExportDataUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository,
    private val appRepository: AppRepository,
    private val backupRepository: BackupRepository,
) : ExportDataUseCase {
    override suspend operator fun invoke(uri: Uri): Result<Unit> {
        return runCatching {
            val data = Backup(
                userSettingsRepository.getUserSettings(),
                DB.version(),
                appRepository.getTargetAppList(),
                appRepository.getFilterConditionList()
            )
            val json = Gson().toJson(data)
            backupRepository.exportToUri(uri, json)
        }
    }
}