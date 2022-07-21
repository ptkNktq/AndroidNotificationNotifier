package me.nya_n.notificationnotifier.views.screen.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.domain.entities.AppException
import me.nya_n.notificationnotifier.domain.entities.Message
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotifyTestUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.utils.Event

class TopViewModel(
    loadAddressUseCase: LoadAddressUseCase,
    private val saveAddressUseCase: SaveAddressUseCase,
    private val notifyTestUseCase: NotifyTestUseCase,
) : ViewModel() {
    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message
    val address = MutableLiveData(loadAddressUseCase())

    /**
     * IPアドレスの保存
     */
    fun save() {
        saveAddressUseCase(address.value)
            .onSuccess {
                _message.postValue(Event(Message.Notice(R.string.addr_saved)))
            }
            .onFailure {
                val error = if (it is AppException) {
                    it.errorTextResourceId
                } else {
                    it.printStackTrace()
                    R.string.unexpected_error
                }
                _message.postValue(Event(Message.Error(error)))
            }
    }

    /**
     * 通知テスト
     */
    fun notifyTest() {
        viewModelScope.launch {
            notifyTestUseCase()
                .onSuccess {
                    _message.postValue(Event(Message.Notice(R.string.notify_test_succeeded)))
                }
                .onFailure {
                    it.printStackTrace()
                    _message.postValue(Event(Message.Error(R.string.notify_test_failed)))
                }
        }
    }
}