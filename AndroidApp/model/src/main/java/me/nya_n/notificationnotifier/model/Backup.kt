package me.nya_n.notificationnotifier.model

data class Backup(
    val setting: UserSetting,
    val version: Int,
    val targets: List<InstalledApp>,
    val filterCondition: List<FilterCondition>
) {
    companion object {
        const val FILE_NAME = "通知配達ドロイド君.json"
    }
}