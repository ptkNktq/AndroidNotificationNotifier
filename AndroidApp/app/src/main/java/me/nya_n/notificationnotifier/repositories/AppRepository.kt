package me.nya_n.notificationnotifier.repositories

import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.domain.entities.FilterCondition
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.repositories.sources.FilterConditionDao
import me.nya_n.notificationnotifier.repositories.sources.TargetAppDao

class AppRepository(
    private val filterConditionDao: FilterConditionDao,
    private val targetAppDao: TargetAppDao,
) {
    /**
     * データの削除
     */
    fun clearAll() {
        filterConditionDao.clear()
        targetAppDao.clear()
    }

    /*
     * フィルタリング条件関連
     */
    suspend fun getFilterCondition(targetPackageName: String): FilterCondition? {
        return withContext(Dispatchers.IO) {
            filterConditionDao.get(targetPackageName)
        }
    }

    suspend fun getFilterConditionList(): List<FilterCondition> {
        return withContext(Dispatchers.IO) {
            filterConditionDao.getAll()
        }
    }

    suspend fun saveFilterCondition(condition: FilterCondition) {
        withContext(Dispatchers.IO) {
            filterConditionDao.insert(condition)
        }
    }


    /*
     * ターゲットアプリ関連
     */
    suspend fun getTargetAppList(): List<InstalledApp> {
        return withContext(Dispatchers.IO) {
            targetAppDao.getAll()
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

    fun loadInstalledAppList(pm: PackageManager): List<InstalledApp> {
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