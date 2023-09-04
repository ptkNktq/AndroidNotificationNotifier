package me.nya_n.notificationnotifier.data.repository.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp

@Database(
    entities = [
        FilterCondition::class,
        InstalledApp::class
    ],
    version = 1,
    exportSchema = false
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

        fun version(): Int {
            return try {
                INSTANCE?.openHelper?.readableDatabase?.version ?: -1
            } catch (e: Exception) {
                -1
            }
        }
    }
}