package me.nya_n.notificationnotifier.data.repository.source

import android.content.SharedPreferences
import androidx.core.content.edit

open class KeyValueDataStore(
    private val pref: SharedPreferences
) {
    protected fun get(key: String, defValue: String): String {
        return pref.getString(key, defValue) ?: defValue
    }

    protected fun get(key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    protected fun get(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    protected fun put(key: String, value: String) {
        pref.edit {
            putString(key, value)
        }
    }

    protected fun put(key: String, value: Int) {
        pref.edit {
            putInt(key, value)
        }
    }

    protected fun put(key: String, value: Boolean) {
        pref.edit {
            putBoolean(key, value)
        }
    }
}