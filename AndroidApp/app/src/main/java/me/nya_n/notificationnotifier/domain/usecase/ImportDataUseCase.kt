package me.nya_n.notificationnotifier.domain.usecase

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.model.Backup
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.repositories.sources.DB
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * 外部ストレージのバックアップからデータを復元
 */
class ImportDataUseCase(
    private val userSettingRepository: UserSettingRepository,
    private val appRepository: AppRepository,
) {
    suspend operator fun invoke(context: Context, uri: Uri): Result<Unit> {
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
            userSettingRepository.saveUserSetting(backup.setting)
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