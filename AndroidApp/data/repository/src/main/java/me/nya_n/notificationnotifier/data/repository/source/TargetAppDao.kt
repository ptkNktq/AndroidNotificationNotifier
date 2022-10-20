package me.nya_n.notificationnotifier.data.repository.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.nya_n.notificationnotifier.model.InstalledApp

@Dao
interface TargetAppDao {
    @Insert
    fun insert(target: InstalledApp)

    @Delete
    fun delete(target: InstalledApp)

    @Query("select * from targets")
    fun getAll(): List<InstalledApp>

    @Query("delete from targets")
    fun clear()
}