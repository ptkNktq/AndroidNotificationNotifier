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
        private const val DB_NAME = "db"

        @Volatile
        private var INSTANCE: DB? = null

        fun get(context: Context, isInMemory: Boolean = false): DB {
            return INSTANCE ?: synchronized(this) {
                val instance = if (isInMemory) {
                    createInMemory(context)
                } else {
                    createInDisk(context)
                }
                INSTANCE = instance
                instance
            }
        }

        private fun createInDisk(context: Context): DB {
            return Room.databaseBuilder(
                context,
                DB::class.java,
                DB_NAME
            ).build()
        }

        private fun createInMemory(context: Context): DB {
            return Room.inMemoryDatabaseBuilder(
                context,
                DB::class.java
            ).build()
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