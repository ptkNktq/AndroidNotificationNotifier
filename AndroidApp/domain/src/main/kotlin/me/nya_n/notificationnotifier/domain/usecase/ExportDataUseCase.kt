package me.nya_n.notificationnotifier.domain.usecase

import android.net.Uri

/** バックアップのために外部ストレージにデータを保存 */
interface ExportDataUseCase {
    suspend operator fun invoke(uri: Uri): Result<Unit>
}