package me.nya_n.notificationnotifier.data.repository

import android.content.pm.PackageManager
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp

interface AppRepository {
    /**
     * データの削除
     */
    suspend fun clearAll()

    /*
     * フィルタリング条件関連
     */
    suspend fun getFilterCondition(targetPackageName: String): FilterCondition?

    suspend fun getFilterConditionList(): List<FilterCondition>

    suspend fun saveFilterCondition(condition: FilterCondition)

    /*
     * ターゲットアプリ関連
     */
    suspend fun getTargetAppList(): List<InstalledApp>

    suspend fun addTargetApp(target: InstalledApp)

    suspend fun deleteTargetApp(target: InstalledApp)

    fun loadInstalledAppList(pm: PackageManager): List<InstalledApp>
}