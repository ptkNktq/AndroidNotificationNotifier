package me.nya_n.notificationnotifier.repositories.sources

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

open class KeyValueDataStore(
    context: Context
) {
    private val pref: SharedPreferences

    init {
        val key = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        pref = EncryptedSharedPreferences.create(
            "settings",
            key,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

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