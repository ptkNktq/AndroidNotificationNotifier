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

/**
 * バックアップのために外部ストレージにデータを保存
 */
class ExportDataUseCase(
    private val userSettingRepository: UserSettingRepository,
    private val appRepository: AppRepository,
) {
    suspend operator fun invoke(context: Context, uri: Uri): Result<Unit> {
        return runCatching {
            val data = Backup(
                userSettingRepository.getUserSetting(),
                DB.version(),
                appRepository.getTargetAppList(),
                appRepository.getFilterConditionList()
            )
            val json = Gson().toJson(data)
            withContext(Dispatchers.IO) {
                context.contentResolver.openOutputStream(uri).use {
                    it?.write(json.toByteArray())
                }
            }
        }
    }
}