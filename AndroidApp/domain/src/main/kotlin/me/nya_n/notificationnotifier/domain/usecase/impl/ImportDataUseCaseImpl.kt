package me.nya_n.notificationnotifier.domain.usecase.impl

import android.net.Uri
import com.google.gson.Gson
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.BackupRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.domain.usecase.ImportDataUseCase
import me.nya_n.notificationnotifier.model.Backup

class ImportDataUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository,
    private val appRepository: AppRepository,
    private val backupRepository: BackupRepository,
) : ImportDataUseCase {
    override suspend operator fun invoke(uri: Uri): Result<Unit> {
        return runCatching {
            val json = backupRepository.importFromUri(uri)
            val backup = Gson().fromJson(json, Backup::class.java)
            if (backup.version != DB.version()) {
                throw RuntimeException("bad version.")
            }
            userSettingsRepository.saveUserSettings(backup.setting)
            appRepository.clearAll()
            backup.targets.forEach {
                appRepository.addTargetApp(it)
            }
            backup.filterCondition.forEach {
                appRepository.saveFilterCondition(it)
            }
        }
    }
}