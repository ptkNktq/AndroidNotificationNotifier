package me.nya_n.notificationnotifier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "conditions")
data class FilterCondition(
    /*
     * フィルタ対象のパッケージ名
     */
    @PrimaryKey
    @ColumnInfo(name = "target_package_name")
    val targetPackageName: String,

    /*
     * 条件
     */
    val condition: String

) : Serializable