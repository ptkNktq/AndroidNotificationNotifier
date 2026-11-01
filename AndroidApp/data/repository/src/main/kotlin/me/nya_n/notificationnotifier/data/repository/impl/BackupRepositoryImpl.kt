package me.nya_n.notificationnotifier.data.repository.impl

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.BackupRepository
import java.io.BufferedReader
import java.io.InputStreamReader

class BackupRepositoryImpl(
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BackupRepository {
    override suspend fun exportToUri(uri: Uri, data: String) {
        withContext(coroutineDispatcher) {
            context.contentResolver.openOutputStream(uri)?.use {
                it.write(data.toByteArray())
            } ?: throw RuntimeException("Failed to open output stream.")
        }
    }

    override suspend fun importFromUri(uri: Uri): String {
        val sb = StringBuilder()
        withContext(coroutineDispatcher) {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BufferedReader(InputStreamReader(input)).use { reader ->
                    sb.append(reader.readLine())
                } ?: throw RuntimeException("Failed to read input stream.")
            } ?: throw RuntimeException("Failed to open input stream.")
        }
        return sb.toString()
    }
}