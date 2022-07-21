package me.nya_n.notificationnotifier.views.screen.detail

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.entities.FilterCondition
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.entities.Message
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.utils.AppIcon
import me.nya_n.notificationnotifier.utils.Event

class DetailViewModel(
    context: Context,
    private val appRepository: AppRepository,
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
    val filterCondition = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            val cond = appRepository.getFilterCondition(target.packageName)
            filterCondition.postValue(cond?.condition ?: "")
        }
    }

    fun deleteTarget() {
        viewModelScope.launch {
            appRepository.deleteTargetApp(target)
            _message.postValue(Event(Message.Notice(R.string.deleted)))
            _targetDeleted.postValue(Event(target))
        }
    }

    fun save() {
        viewModelScope.launch {
            val cond = filterCondition.value ?: ""
            appRepository.saveFilterCondition(FilterCondition(target.packageName, cond))
            _message.postValue(Event(Message.Notice(R.string.condition_added)))
        }
    }
}