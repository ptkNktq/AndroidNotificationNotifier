package me.nya_n.notificationnotifier.viewmodels

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.entities.Message
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.utils.AppIcon
import me.nya_n.notificationnotifier.utils.Event

class DetailViewModel(
    context: Context,
    private val userSettingRepository: UserSettingRepository,
    private val target: InstalledApp
) : ViewModel() {
    val targetIcon = liveData {
        emit(AppIcon.get(target.packageName, context.packageManager))
    }
    val targetName = liveData {
        emit(target.label)
    }
    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message
    private val _targetDeleted = MutableLiveData<Event<InstalledApp>>()
    val targetDeleted: LiveData<Event<InstalledApp>> = _targetDeleted

    fun deleteTarget() {
        viewModelScope.launch {
            val newTargets = userSettingRepository.getUserSetting()
                .targets
                .filter {
                    it != target.packageName
                }
            val setting = userSettingRepository.getUserSetting()
                .copy(targets = newTargets)
            userSettingRepository.saveUserSetting(setting)
            _message.postValue(Event(Message.Notice(R.string.deleted)))
            _targetDeleted.postValue(Event(target))
        }
    }
}