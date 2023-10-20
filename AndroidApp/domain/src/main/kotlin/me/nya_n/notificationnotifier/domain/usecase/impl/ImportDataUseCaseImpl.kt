package me.nya_n.notificationnotifier.domain.usecase.impl

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.domain.usecase.ImportDataUseCase
import me.nya_n.notificationnotifier.model.Backup
import java.io.BufferedReader
import java.io.InputStreamReader

class ImportDataUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository,
    private val appRepository: AppRepository,
) : ImportDataUseCase {
    override suspend operator fun invoke(context: Context, uri: Uri): Result<Unit> {
        return runCatching {
            val sb = StringBuilder()
            withContext(Dispatchers.IO) {
                context.contentResolver.openInputStream(uri).use { input ->
                    BufferedReader(InputStreamReader(input)).use { reader ->
                        sb.append(reader.readLine())
                    }
                }
            }
            val json = sb.toString()
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