package kani.koubou.net.notificationnotifier

import android.content.SharedPreferences

class SPHelper(val sp: SharedPreferences)
{
    /*
     * キー
     */
    val KEY_PORT = "port"
    val KEY_IP = "ip"
    val KEY_TARGETS = "targets"

    fun getPort() : Int
    {
        return sp.getInt(KEY_PORT, 8000)
    }

    fun setPort(port: Int)
    {
        sp.edit().putInt(KEY_PORT, port).apply()
    }

    fun getIp() : String
    {
        return sp.getString(KEY_IP, "")
    }

    fun setIp(ip: String)
    {
        sp.edit().putString(KEY_IP, ip).apply()
    }

    fun getTargets() : String
    {
        return sp.getString(KEY_TARGETS, "")
    }

    fun setTargets(targets: String)
    {
        sp.edit().putString(KEY_TARGETS, targets).apply()
    }
}