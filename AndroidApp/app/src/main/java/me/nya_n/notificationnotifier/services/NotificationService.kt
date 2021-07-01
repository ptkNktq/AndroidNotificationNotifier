package me.nya_n.notificationnotifier.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import kotlinx.coroutines.*
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class NotificationService : NotificationListenerService() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        notify(sbn)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun notify(sbn: StatusBarNotification) {
        scope.launch {
            val extra = sbn.notification.extras
            val title = getTitle(extra.get("android.title")) ?: return@launch
            val text = extra.getCharSequence("android.text").toString()

            val userSettingRepository = UserSettingRepository(applicationContext)
            val setting = userSettingRepository.getUserSetting()
            if (!setting.targets.contains(sbn.packageName)) {
                return@launch
            }

            val appRepository = AppRepository(applicationContext)
            val cond = appRepository.getFilterCondition(sbn.packageName)
            if (cond != null && cond.condition.isNotEmpty()) {
                val regex = Regex(pattern = cond.condition)
                if (!regex.matches("$title $text")) {
                    return@launch
                }
            }

            withContext(Dispatchers.IO) {
                val message = "${title}\n${text}"
                val buff = message.toByteArray()
                val addr = InetAddress.getByName(setting.host)
                val packet = DatagramPacket(buff, buff.size, addr, setting.port)
                DatagramSocket().apply {
                    send(packet)
                    close()
                }
            }
        }
    }

    private fun getTitle(title: Any?): String? {
        return when (title) {
            is String -> title
            is SpannableString -> title.toString()
            else -> null
        }
    }
}