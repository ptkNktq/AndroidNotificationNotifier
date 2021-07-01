package me.nya_n.notificationnotifier.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.entities.Message
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.utils.Event

class SelectionViewModel(
    private val userSettingRepository: UserSettingRepository
) : ViewModel() {
    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message
    private val _targetAdded = MutableLiveData<Event<InstalledApp>>()
    val targetAdded: LiveData<Event<InstalledApp>> = _targetAdded
    val query = MutableLiveData("")

    fun addTarget(target: InstalledApp) {
        viewModelScope.launch {
            val newTargets = userSettingRepository.getUserSetting()
                .targets
                .toMutableList()
                .apply {
                    if (!contains(target.packageName)) {
                        add(target.packageName)
                    }
                }
            val setting = userSettingRepository.getUserSetting()
                .copy(targets = newTargets)
            userSettingRepository.saveUserSetting(setting)
            _message.postValue(Event(Message.Notice(R.string.added)))
            _targetAdded.postValue(Event(target))
        }
    }
}