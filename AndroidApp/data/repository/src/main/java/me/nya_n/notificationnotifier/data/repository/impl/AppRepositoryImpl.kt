package me.nya_n.notificationnotifier.data.repository.impl

import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.source.FilterConditionDao
import me.nya_n.notificationnotifier.data.repository.source.TargetAppDao
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp

class AppRepositoryImpl(
    private val filterConditionDao: FilterConditionDao,
    private val targetAppDao: TargetAppDao,
) : AppRepository {
    override suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            filterConditionDao.clear()
            targetAppDao.clear()
        }
    }

    override suspend fun getFilterCondition(targetPackageName: String): FilterCondition? {
        return withContext(Dispatchers.IO) {
            filterConditionDao.get(targetPackageName)
        }
    }

    override suspend fun getFilterConditionList(): List<FilterCondition> {
        return withContext(Dispatchers.IO) {
            filterConditionDao.getAll()
        }
    }

    override suspend fun saveFilterCondition(condition: FilterCondition) {
        withContext(Dispatchers.IO) {
            filterConditionDao.insert(condition)
        }
    }

    override suspend fun getTargetAppList(): List<InstalledApp> {
        return withContext(Dispatchers.IO) {
            targetAppDao.getAll()
        }
    }

    override suspend fun addTargetApp(target: InstalledApp) {
        withContext(Dispatchers.IO) {
            targetAppDao.insert(target)
        }
    }

    override suspend fun deleteTargetApp(target: InstalledApp) {
        withContext(Dispatchers.IO) {
            targetAppDao.delete(target)
        }
    }

    override fun loadInstalledAppList(pm: PackageManager): List<InstalledApp> {
        return pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .map {
                val label = pm.getApplicationLabel(it).toString()
                InstalledApp(
                    label,
                    it.packageName
                )
            }
    }
}