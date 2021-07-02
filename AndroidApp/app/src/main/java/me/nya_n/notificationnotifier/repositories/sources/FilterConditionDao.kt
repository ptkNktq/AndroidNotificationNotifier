package me.nya_n.notificationnotifier.repositories.sources

import androidx.room.*
import me.nya_n.notificationnotifier.entities.FilterCondition

@Dao
interface FilterConditionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cond: FilterCondition)

    @Delete
    fun delete(cond: FilterCondition)

    @Query("select * from conditions where target_package_name = :targetPackageName")
    fun get(targetPackageName: String): FilterCondition?
}