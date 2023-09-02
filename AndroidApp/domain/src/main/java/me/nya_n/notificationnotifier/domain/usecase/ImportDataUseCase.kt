package me.nya_n.notificationnotifier.domain.usecase

import android.content.Context
import android.net.Uri

/**
 * 外部ストレージのバックアップからデータを復元
 */
interface ImportDataUseCase {
    suspend operator fun invoke(context: Context, uri: Uri): Result<Unit>
}