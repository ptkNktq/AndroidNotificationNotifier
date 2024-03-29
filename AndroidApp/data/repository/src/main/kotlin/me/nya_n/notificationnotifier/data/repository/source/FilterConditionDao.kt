package me.nya_n.notificationnotifier.data.repository.source

import androidx.room.*
import me.nya_n.notificationnotifier.model.FilterCondition

@Dao
interface FilterConditionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cond: FilterCondition)

    @Delete
    fun delete(cond: FilterCondition)

    @Query("select * from conditions where target_package_name = :targetPackageName")
    fun get(targetPackageName: String): FilterCondition?

    @Query("select * from conditions")
    fun getAll(): List<FilterCondition>

    @Query("delete from conditions")
    fun clear()
}