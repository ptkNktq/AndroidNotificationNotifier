package me.nya_n.notificationnotifier.domain.usecase.impl

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.domain.usecase.ExportDataUseCase
import me.nya_n.notificationnotifier.model.Backup

class ExportDataUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository,
    private val appRepository: AppRepository,
) : ExportDataUseCase {
    override suspend operator fun invoke(context: Context, uri: Uri): Result<Unit> {
        return runCatching {
            val data = Backup(
                userSettingsRepository.getUserSettings(),
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