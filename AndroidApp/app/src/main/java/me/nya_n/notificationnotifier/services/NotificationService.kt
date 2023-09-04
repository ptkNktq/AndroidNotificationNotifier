package me.nya_n.notificationnotifier.services

import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import kotlinx.coroutines.*
import me.nya_n.notificationnotifier.domain.usecase.NotifyTargetAppNotificationUseCase
import org.koin.android.ext.android.inject

class NotificationService : NotificationListenerService() {

    private val useCase: NotifyTargetAppNotificationUseCase by inject()
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
            val extras = sbn.notification.extras
            val title = getTitle(extras) ?: return@launch
            val message = extras.getCharSequence("android.text").toString()
            useCase(sbn.packageName, title, message)
        }
    }

    @Suppress("DEPRECATION")
    private fun getTitle(extras: Bundle): String? {
        // titleはStringとSpannableStringの可能性があったのでBundle#getを使っている
        // 何かいい方法があったらdeprecateを解消したい
        return when (val title = extras.get("android.title")) {
            is String -> title
            is SpannableString -> title.toString()
            else -> null
        }
    }
}