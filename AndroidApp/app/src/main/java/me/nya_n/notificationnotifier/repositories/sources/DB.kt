package me.nya_n.notificationnotifier.repositories.sources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.nya_n.notificationnotifier.domain.entities.FilterCondition
import me.nya_n.notificationnotifier.domain.entities.InstalledApp

@Database(
    entities = [
        FilterCondition::class,
        InstalledApp::class
    ],
    version = 1
)
abstract class DB : RoomDatabase() {

    abstract fun filterConditionDao(): FilterConditionDao

    abstract fun targetAppDao(): TargetAppDao

    companion object {
        @Volatile
        private var INSTANCE: DB? = null

        fun get(context: Context): DB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    DB::class.java,
                    "db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun version(context: Context): Int {
            return try {
                get(context).openHelper.readableDatabase.version
            } catch (e: Exception) {
                -1
            }
        }
    }
}