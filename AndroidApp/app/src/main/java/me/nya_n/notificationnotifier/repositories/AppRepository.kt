package me.nya_n.notificationnotifier.repositories

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.entities.FilterCondition
import me.nya_n.notificationnotifier.repositories.sources.DB

class AppRepository(
    context: Context
) {
    private val dao by lazy {
        Room.databaseBuilder(context, DB::class.java, "db")
            .build()
            .filterConditionDao()
    }

    suspend fun getFilterCondition(targetPackageName: String): FilterCondition? {
        return withContext(Dispatchers.IO) {
            dao.get(targetPackageName)
        }
    }

    suspend fun saveFilterCondition(condition: FilterCondition) {
        withContext(Dispatchers.IO) {
            dao.insert(condition)
        }
    }
}