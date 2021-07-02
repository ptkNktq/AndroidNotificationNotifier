package me.nya_n.notificationnotifier.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.entities.FilterCondition
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.repositories.sources.DB

class AppRepository(
    context: Context
) {
    private val filterConditionDao by lazy {
        DB.get(context).filterConditionDao()
    }

    private val targetAppDao by lazy {
        DB.get(context).targetAppDao()
    }

    suspend fun getFilterCondition(targetPackageName: String): FilterCondition? {
        return withContext(Dispatchers.IO) {
            filterConditionDao.get(targetPackageName)
        }
    }

    suspend fun saveFilterCondition(condition: FilterCondition) {
        withContext(Dispatchers.IO) {
            filterConditionDao.insert(condition)
        }
    }

    suspend fun getTargetAppList(): List<InstalledApp> {
        return withContext(Dispatchers.IO) {
            targetAppDao.get()
        }
    }

    suspend fun addTargetApp(target: InstalledApp) {
        withContext(Dispatchers.IO) {
            targetAppDao.insert(target)
        }
    }

    suspend fun deleteTargetApp(target: InstalledApp) {
        withContext(Dispatchers.IO) {
            targetAppDao.delete(target)
        }
    }
}