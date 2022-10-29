package me.nya_n.notificationnotifier.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import kotlinx.coroutines.*
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase
import org.koin.android.ext.android.inject

class NotificationService : NotificationListenerService() {

    private val appRepository: AppRepository by inject()
    private val useCase: NotifyUseCase by inject()
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

            val targets = appRepository.getTargetAppList()
            if (!targets.any { t -> t.packageName == sbn.packageName }) {
                return@launch
            }

            val cond = appRepository.getFilterCondition(sbn.packageName)
            if (cond != null && cond.condition.isNotEmpty()) {
                val regex = Regex(pattern = cond.condition)
                if (!regex.matches("$title $text")) {
                    return@launch
                }
            }

            withContext(Dispatchers.IO) {
                val message = "${title}\n${text}"
                useCase(message)
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