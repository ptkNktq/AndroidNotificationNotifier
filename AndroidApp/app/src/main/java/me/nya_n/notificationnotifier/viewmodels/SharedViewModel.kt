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
import me.nya_n.notificationnotifier.repositories.UserSettingRepository

class SharedViewModel(
    context: Context,
    private val userSettingRepository: UserSettingRepository
) : ViewModel() {
    private val pm = context.packageManager
    private val _list = MutableLiveData<List<InstalledApp>>()
    val list: LiveData<List<InstalledApp>> = _list
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _targets = MutableLiveData<List<String>>()
    val targets: LiveData<List<String>> = _targets

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
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

            val setting = userSettingRepository.getUserSetting()
            _targets.postValue(setting.targets)
            _isLoading.postValue(false)
        }
    }
}