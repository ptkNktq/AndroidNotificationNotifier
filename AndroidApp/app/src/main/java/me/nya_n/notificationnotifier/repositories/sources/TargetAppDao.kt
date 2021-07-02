package me.nya_n.notificationnotifier.repositories.sources

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.nya_n.notificationnotifier.entities.InstalledApp

@Dao
interface TargetAppDao {
    @Insert
    fun insert(target: InstalledApp)

    @Delete
    fun delete(target: InstalledApp)

    @Query("select * from targets")
    fun get(): List<InstalledApp>
}