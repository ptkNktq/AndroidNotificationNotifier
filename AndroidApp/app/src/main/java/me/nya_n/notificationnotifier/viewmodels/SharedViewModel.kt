package me.nya_n.notificationnotifier.viewmodels

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.entities.Message
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.utils.Event

class SharedViewModel(
    context: Context,
    private val userSettingRepository: UserSettingRepository
) : ViewModel() {

    private val _list = MutableLiveData<List<InstalledApp>>()
    val list: LiveData<List<InstalledApp>> = _list
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _targets = MutableLiveData<List<String>>()
    val targets: LiveData<List<String>> = _targets
    private val _deletedMessage = MutableLiveData<Event<Message>>()
    val deletedMessage: LiveData<Event<Message>> = _deletedMessage
    private val _addedMessage = MutableLiveData<Event<Message>>()
    val addedMessage: LiveData<Event<Message>> = _addedMessage

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val pm = context.packageManager
            val apps = withContext(Dispatchers.IO) {
                pm.getInstalledApplications(PackageManager.GET_META_DATA)
                    .map {
                        val label = pm.getApplicationLabel(it).toString()
                        val icon = pm.getApplicationIcon(it)
                        InstalledApp(
                            label,
                            it.packageName,
                            icon
                        )
                    }
            }
            _list.postValue(apps)

            val setting = userSettingRepository.getUserSetting()
            _targets.postValue(setting.targets)
            _isLoading.postValue(false)
        }
    }

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
            targetChanged(newTargets)
            _addedMessage.postValue(Event(Message.Notice(R.string.added)))
        }
    }

    fun deleteTarget(target: InstalledApp) {
        viewModelScope.launch {
            val newTargets = userSettingRepository.getUserSetting()
                .targets
                .filter {
                    it != target.packageName
                }
            targetChanged(newTargets)
            _deletedMessage.postValue(Event(Message.Notice(R.string.deleted)))
        }
    }

    private fun targetChanged(targets: List<String>) {
        val setting = userSettingRepository.getUserSetting()
            .copy(targets = targets)
        userSettingRepository.saveUserSetting(setting)
        _targets.postValue(targets)
    }
}