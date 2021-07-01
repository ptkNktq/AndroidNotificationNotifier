package me.nya_n.notificationnotifier.repositories.sources

import androidx.room.Database
import androidx.room.RoomDatabase
import me.nya_n.notificationnotifier.entities.FilterCondition

@Database(entities = [FilterCondition::class], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun filterConditionDao(): FilterConditionDao
}