package me.nya_n.notificationnotifier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "targets")
data class InstalledApp(
    val label: String,

    @PrimaryKey
    @ColumnInfo(name = "package_name")
    @SerializedName("package_name")
    val packageName: String
) : Serializable