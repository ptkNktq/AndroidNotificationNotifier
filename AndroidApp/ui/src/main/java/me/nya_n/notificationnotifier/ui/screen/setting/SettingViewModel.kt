package me.nya_n.notificationnotifier.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.model.Message
import me.nya_n.notificationnotifier.ui.R

class SettingViewModel(
    loadAddressUseCase: LoadAddressUseCase,
    private val saveAddressUseCase: SaveAddressUseCase,
    private val notifyUseCase: NotifyUseCase,
) : ViewModel() {

    /**
     * UIの状態
     */
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(address = loadAddressUseCase()) }
        }
    }

    /**
     * アドレスの更新
     * @param address 変更後のアドレス
     */
    fun updateAddress(address: String) {
        /* FIXME:
         *  正しい形式(host:port)以外は保存されない、これはちょっと見直した方が良さそう
         */
        saveAddressUseCase(address)
        _uiState.update { it.copy(address = address) }
    }

    /**
     * 通知テスト
     */
    fun notifyTest() {
        viewModelScope.launch {
            val message = if (notifyUseCase("通知テスト").isSuccess) {
                Message.Notice(R.string.notify_test_succeeded)
            } else {
                Message.Error(R.string.notify_test_failed)
            }
            _uiState.update { it.copy(message = message) }
        }
    }

    /**
     * メッセージを表示した
     */
    fun messageShown() {
        _uiState.update { it.copy(message = null) }
    }
}