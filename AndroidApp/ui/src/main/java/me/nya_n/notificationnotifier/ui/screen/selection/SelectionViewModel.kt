package me.nya_n.notificationnotifier.ui.screen.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.domain.AddTargetAppUseCase
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.util.Event

class SelectionViewModel(
    private val addTargetAppUseCase: AddTargetAppUseCase
) : ViewModel() {
    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message
    private val _targetAdded = MutableLiveData<Event<InstalledApp>>()
    val targetAdded: LiveData<Event<InstalledApp>> = _targetAdded
    val query = MutableLiveData("")

    fun addTarget(target: InstalledApp) {
        viewModelScope.launch {
            addTargetAppUseCase(target)
            _message.postValue(Event(Message.Notice(R.string.added)))
            _targetAdded.postValue(Event(target))
        }
    }
}