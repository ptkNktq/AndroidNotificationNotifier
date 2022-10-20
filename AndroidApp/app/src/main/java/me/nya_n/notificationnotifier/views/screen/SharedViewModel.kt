package me.nya_n.notificationnotifier.views.screen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.domain.LoadAppUseCase
import me.nya_n.notificationnotifier.domain.PackageVisibilityGrantedUseCase
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.utils.Event

class SharedViewModel(
    context: Context,
    private val packageVisibilityGrantedUseCase: PackageVisibilityGrantedUseCase,
    private val loadAppUseCase: LoadAppUseCase,
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
            _isLoading.postValue(true)
            loadAppUseCase(pm)
                .onSuccess {
                    _list.postValue(it.installs)
                    _targets.postValue(it.targets)
                }
                .onFailure {
                    _checkPackageVisibilityEvent.postValue(Event(true))
                }
            _isLoading.postValue(false)
        }
    }

    fun packageVisibilityGranted() {
        packageVisibilityGrantedUseCase()
    }
}