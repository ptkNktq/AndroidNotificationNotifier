package me.nya_n.notificationnotifier.data.repository

import android.net.Uri

interface BackupRepository {
    /** [uri]に通知対象や条件、設定を保存
     *  @param uri 保存先
     *  @param data 保存するデータ
     */
    suspend fun exportToUri(uri: Uri, data: String)

    /** [uri]から通知対象や条件、設定を復元
     *  @param uri 読み込み元
     *  @return 復元したデータ
     */
    suspend fun importFromUri(uri: Uri): String
}