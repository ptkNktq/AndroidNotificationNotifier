package kani.koubou.net.notificationnotifier.service

import android.preference.PreferenceManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import kani.koubou.net.notificationnotifier.SPHelper
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class NotificationService : NotificationListenerService()
{
    override fun onNotificationPosted(sbn: StatusBarNotification)
    {
        val spHelper = SPHelper(PreferenceManager.getDefaultSharedPreferences(this))
        val ex = sbn.notification.extras
        val title = getTitle(ex.get("android.title"))
        val text = ex.getCharSequence("android.text")

        if(title.isNullOrBlank() || text.isNullOrBlank() || !spHelper.getTargets().split(",").contains(sbn.packageName))
        {
            return
        }

        val message = title + "\n" + text
        val buff = message.toByteArray();
        val addr = InetAddress.getByName(spHelper.getIp())
        val packet = DatagramPacket(buff, buff.size, addr, spHelper.getPort())
        val socket = DatagramSocket()
        socket.send(packet)
        socket.close()
        println(sbn.packageName + "\n" + message + "\n")
    }

    fun getTitle(title: Any) : String
    {
        return when(title)
        {
            is String -> title
            is SpannableString -> title.toString()
            else -> throw Exception()
        }
    }
}