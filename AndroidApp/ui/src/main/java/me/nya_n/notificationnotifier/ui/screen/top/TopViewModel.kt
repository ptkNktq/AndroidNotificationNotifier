package me.nya_n.notificationnotifier.ui.screen.top

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.domain.*
import me.nya_n.notificationnotifier.model.AppException
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.util.Event

class TopViewModel(
    loadAddressUseCase: LoadAddressUseCase,
    private val saveAddressUseCase: SaveAddressUseCase,
    private val notifyTestUseCase: NotifyTestUseCase,
    private val importDataUseCase: ImportDataUseCase,
    private val exportDataUseCase: ExportDataUseCase,
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

    /**
     * バックアップのために外部ストレージにデータを保存
     */
    fun exportData(context: Context, uri: Uri) {
        viewModelScope.launch {
            exportDataUseCase(context, uri)
                .onSuccess {
                    _message.postValue(Event(Message.Notice(R.string.export_succeeded)))
                }
                .onFailure {
                    it.printStackTrace()
                    _message.postValue(Event(Message.Error(R.string.export_failed)))
                }
        }
    }

    /**
     * 外部ストレージのバックアップからデータを復元
     */
    fun importData(context: Context, uri: Uri) {
        viewModelScope.launch {
            importDataUseCase(context, uri)
                .onSuccess {
                    _message.postValue(Event(Message.Notice(R.string.import_succeeded)))
                }
                .onFailure {
                    it.printStackTrace()
                    _message.postValue(Event(Message.Error(R.string.import_failed)))
                }
        }
    }
}