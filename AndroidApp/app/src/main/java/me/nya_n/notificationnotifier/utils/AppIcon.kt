package me.nya_n.notificationnotifier.utils

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.core.graphics.drawable.toBitmap

object AppIcon {
    const val CACHE_SIZE = 4 * 1024 * 1024
    private val cache = object : LruCache<String, Bitmap>(CACHE_SIZE) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount
        }
    }

    fun get(packageName: String, pm: PackageManager): Bitmap {
        var img = cache[packageName]
        if (img == null) {
            img = pm.getApplicationIcon(packageName).toBitmap()
            cache.put(packageName, img)
        }
        return img
    }
}