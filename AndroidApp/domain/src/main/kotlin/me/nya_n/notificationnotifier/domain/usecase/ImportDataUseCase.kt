package me.nya_n.notificationnotifier.domain.usecase

import android.net.Uri

/** 外部ストレージのバックアップからデータを復元 */
interface ImportDataUseCase {
    suspend operator fun invoke(uri: Uri): Result<Unit>
}