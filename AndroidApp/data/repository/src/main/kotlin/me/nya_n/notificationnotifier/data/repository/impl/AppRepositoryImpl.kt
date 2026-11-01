package me.nya_n.notificationnotifier.data.repository.impl

import android.content.pm.PackageManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.source.FilterConditionDao
import me.nya_n.notificationnotifier.data.repository.source.TargetAppDao
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp

class AppRepositoryImpl(
    private val packageManager: PackageManager,
    private val filterConditionDao: FilterConditionDao,
    private val targetAppDao: TargetAppDao,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppRepository {
    override suspend fun clearAll() {
        withContext(coroutineDispatcher) {
            filterConditionDao.clear()
            targetAppDao.clear()
        }
    }

    override suspend fun getFilterCondition(targetPackageName: String): FilterCondition? {
        return withContext(coroutineDispatcher) {
            filterConditionDao.get(targetPackageName)
        }
    }

    override suspend fun getFilterConditionOrDefault(targetPackageName: String): FilterCondition {
        return getFilterCondition(targetPackageName) ?: FilterCondition.default(targetPackageName)
    }

    override suspend fun getFilterConditionList(): List<FilterCondition> {
        return withContext(coroutineDispatcher) {
            filterConditionDao.getAll()
        }
    }

    override suspend fun saveFilterCondition(condition: FilterCondition) {
        withContext(coroutineDispatcher) {
            filterConditionDao.insert(condition)
        }
    }

    override suspend fun getTargetAppList(): List<InstalledApp> {
        return withContext(coroutineDispatcher) {
            targetAppDao.getAll()
        }
    }

    override suspend fun addTargetApp(target: InstalledApp) {
        withContext(coroutineDispatcher) {
            targetAppDao.insert(target)
        }
    }

    override suspend fun deleteTargetApp(target: InstalledApp) {
        withContext(coroutineDispatcher) {
            targetAppDao.delete(target)
        }
    }

    override fun loadInstalledAppList(): List<InstalledApp> {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .map {
                val label = packageManager.getApplicationLabel(it).toString()
                InstalledApp(
                    label,
                    it.packageName
                )
            }
            .sortedBy { it.label }
    }
}