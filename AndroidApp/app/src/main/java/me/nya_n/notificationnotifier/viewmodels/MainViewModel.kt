package me.nya_n.notificationnotifier.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.entities.Backup
import me.nya_n.notificationnotifier.entities.Fab
import me.nya_n.notificationnotifier.entities.Message
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.repositories.sources.DB
import me.nya_n.notificationnotifier.utils.Event
import java.io.BufferedReader
import java.io.InputStreamReader

class MainViewModel(
    private val userSettingRepository: UserSettingRepository,
    private val appRepository: AppRepository
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
            runCatching {
                val data = Backup(
                    userSettingRepository.getUserSetting(),
                    DB.version(context),
                    appRepository.getTargetAppList(),
                    appRepository.getFilterConditionList()
                )
                val json = Gson().toJson(data)
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri).use {
                        it?.write(json.toByteArray())
                    }
                }
            }.onSuccess {
                _message.postValue(
                    Event(
                        Message.Notice(
                            R.string.export_succeeded
                        )
                    )
                )
            }.onFailure {
                it.printStackTrace()
                _message.postValue(
                    Event(
                        Message.Error(
                            R.string.export_failed
                        )
                    )
                )
            }
        }
    }

    /**
     * 外部ストレージのバックアップからデータを復元
     */
    fun importData(context: Context, uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val sb = StringBuilder()
                withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri).use { input ->
                        BufferedReader(InputStreamReader(input)).use { reader ->
                            sb.append(reader.readLine())
                        }
                    }
                }
                val json = sb.toString()
                val backup = Gson().fromJson(json, Backup::class.java)
                if (backup.version != DB.version(context)) {
                    throw RuntimeException("bad version.")
                }
                userSettingRepository.saveUserSetting(backup.setting)
                backup.targets.forEach {
                    appRepository.addTargetApp(it)
                }
                backup.filterCondition.forEach {
                    appRepository.saveFilterCondition(it)
                }
            }.onSuccess {
                _message.postValue(
                    Event(
                        Message.Notice(
                            R.string.import_succeeded
                        )
                    )
                )
            }.onFailure {
                it.printStackTrace()
                _message.postValue(
                    Event(
                        Message.Error(
                            R.string.import_failed
                        )
                    )
                )
            }
        }
    }
}