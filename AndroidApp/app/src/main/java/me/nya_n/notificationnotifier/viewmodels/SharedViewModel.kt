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
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.utils.Event

class SharedViewModel(
    context: Context,
    private val userSettingRepository: UserSettingRepository,
    private val appRepository: AppRepository
) : ViewModel() {
    private val pm = context.packageManager
    private val _list = MutableLiveData<List<InstalledApp>>()
    val list: LiveData<List<InstalledApp>> = _list
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _targets = MutableLiveData<List<InstalledApp>>()
    val targets: LiveData<List<InstalledApp>> = _targets
    private val _checkPackageVisibilityEvent = MutableLiveData<Event<Boolean>>()
    val checkPackageVisibilityEvent: LiveData<Event<Boolean>> = _checkPackageVisibilityEvent

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            val setting = userSettingRepository.getUserSetting()
            if (!setting.isPackageVisibilityGranted) {
                _checkPackageVisibilityEvent.postValue(Event(true))
            } else {
                _isLoading.postValue(true)
                val apps = withContext(Dispatchers.IO) {
                    pm.getInstalledApplications(PackageManager.GET_META_DATA)
                        .map {
                            val label = pm.getApplicationLabel(it).toString()
                            InstalledApp(
                                label,
                                it.packageName
                            )
                        }
                }
                _list.postValue(apps)
                _targets.postValue(appRepository.getTargetAppList())
                _isLoading.postValue(false)
            }
        }
    }

    fun packageVisibilityGranted() {
        viewModelScope.launch {
            val setting = userSettingRepository.getUserSetting()
                .copy(isPackageVisibilityGranted = true)
            userSettingRepository.saveUserSetting(setting)
        }
    }
}