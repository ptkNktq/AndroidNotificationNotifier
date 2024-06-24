package me.nya_n.notificationnotifier.ui.util

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

object AppIconCache {
    private val ICON_SIZE_DEFAULT = 56.dp.value.toInt()
    private const val CACHE_SIZE = 4 * 1024 * 1024

    private val cache = object : LruCache<String, Bitmap>(CACHE_SIZE) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount
        }
    }

    /** アプリのアイコンを取得する
     *  @param packageName パッケージ名
     *  @param pm パッケージマネージャー
     *  @param isInListView ListViewの中で表示しているか
     *
     *  @return アプリのアイコン
     *
     *  FIXME: TODO:
     *   ListViewにて画像読み込みタイミング次第では
     *   「IllegalArgumentException: width and height must be > 0」になってクラッシュする
     *   画像表示対象のImageがスクロールによって画面外になりintrinsicWidthが0になっているのでは？分かりゃん…
     *   これの対策としてListView内の場合は固定サイズで読み込むことで一時的に対処している
     */
    fun get(
        packageName: String,
        pm: PackageManager,
        isInListView: Boolean
    ): Bitmap {
        /* FIXME: TODO:
         *  isInListViewによって画像読み込みサイズが異なることがあるので、それも考慮してキャッシュする
         *  ただしisInListViewがfalseのとき、最初に読み込んだサイズでキャッシュされるので、
         *  完全に同じ解像度を取得できるわけではない
         *  ex)
         *   1回目: isInListView:false, intrinsicWidth:32
         *   2回目: isInListView:false, intrinsicWidth:56
         *   このとき、2回目はサイズ32の画像が返ってくる
         */
        val key = "$packageName-$isInListView"
        var img = cache[key]
        if (img == null) {
            val icon = pm.getApplicationIcon(packageName)
            img = if (isInListView) {
                icon.toBitmap(width = ICON_SIZE_DEFAULT, height = ICON_SIZE_DEFAULT)
            } else {
                icon.toBitmap()
            }
            cache.put(key, img)
        }
        return img
    }
}