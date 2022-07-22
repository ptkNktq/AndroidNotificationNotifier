package me.nya_n.notificationnotifier.views.screen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.domain.entities.Fab
import me.nya_n.notificationnotifier.domain.entities.Message
import me.nya_n.notificationnotifier.domain.usecase.ExportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.ImportDataUseCase
import me.nya_n.notificationnotifier.utils.Event

class MainViewModel(
    private val importDataUseCase: ImportDataUseCase,
    private val exportDataUseCase: ExportDataUseCase,
) : ViewModel() {
    private val _fab = MutableLiveData<Event<Fab>>()
    val fab: LiveData<Event<Fab>> = _fab
    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message

    /**
     * Fabの状態を更新
     */
    fun changeFabState(fab: Fab) {
        _fab.postValue(Event(fab))
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