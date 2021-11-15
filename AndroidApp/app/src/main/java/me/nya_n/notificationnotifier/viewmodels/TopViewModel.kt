package me.nya_n.notificationnotifier.viewmodels

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.entities.Message
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.repositories.sources.UserSettingDataStore
import me.nya_n.notificationnotifier.utils.Event
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class TopViewModel(
    private val userSettingRepository: UserSettingRepository
) : ViewModel() {
    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message
    val address = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            val setting = userSettingRepository.getUserSetting()
            val port = if (setting.port == UserSettingDataStore.DEFAULT_PORT) {
                ""
            } else {
                setting.port
            }
            val addr = "${setting.host}:${port}"
            address.postValue(if (addr.length == 1) "" else addr)
        }
    }

    fun save() {
        viewModelScope.launch {
            val addr = (address.value ?: "").split(":")
            if (addr.size != 2 || !(addr[1].isNotEmpty() && addr[1].isDigitsOnly())) {
                _message.postValue(Event(Message.Error(R.string.validation_error_addr)))
                return@launch
            }
            val setting = userSettingRepository.getUserSetting()
                .copy(
                    host = addr[0],
                    port = addr[1].toInt()
                )
            userSettingRepository.saveUserSetting(setting)
            _message.postValue(Event(Message.Notice(R.string.addr_saved)))
        }
    }

    fun notifyTest() {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    val setting = userSettingRepository.getUserSetting()
                    val buff = "通知テスト".toByteArray()
                    val addr = InetAddress.getByName(setting.host)
                    val packet = DatagramPacket(buff, buff.size, addr, setting.port)
                    DatagramSocket().apply {
                        send(packet)
                        close()
                    }
                }
            }.onSuccess {
                _message.postValue(Event(Message.Notice(R.string.notify_test_succeeded)))
            }.onFailure {
                it.printStackTrace()
                _message.postValue(Event(Message.Error(R.string.notify_test_failed)))
            }
        }
    }
}