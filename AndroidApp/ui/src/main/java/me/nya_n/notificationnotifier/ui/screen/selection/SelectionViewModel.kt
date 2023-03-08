package me.nya_n.notificationnotifier.ui.screen.selection

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.domain.usecase.AddTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadAppUseCase
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.util.Event

class SelectionViewModel(
    context: Context,
    private val loadAppUseCase: LoadAppUseCase,
    private val addTargetAppUseCase: AddTargetAppUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message
    private val _targetAdded = MutableLiveData<Event<InstalledApp>>()
    val targetAdded: LiveData<Event<InstalledApp>> = _targetAdded
    val query = MutableLiveData("")

    init {
        viewModelScope.launch {
            loadAppUseCase(context.packageManager).onSuccess { res ->
                _uiState.update { it.copy(items = res.installs) }
            }
        }
    }

    fun addTarget(target: InstalledApp) {
        viewModelScope.launch {
            addTargetAppUseCase(target)
            _message.postValue(Event(Message.Notice(R.string.added)))
            _targetAdded.postValue(Event(target))
        }
    }
}