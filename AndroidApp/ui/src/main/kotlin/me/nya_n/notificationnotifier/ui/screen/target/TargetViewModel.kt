package me.nya_n.notificationnotifier.ui.screen.target

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.domain.usecase.LoadAppUseCase
import me.nya_n.notificationnotifier.model.Message

class TargetViewModel(
    context: Context,
    private val useCase: LoadAppUseCase
) : ViewModel() {
    private val pm = context.packageManager
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /** 通知対象一覧の取得 */
    fun loadTargets() {
        viewModelScope.launch {
            if (uiState.value.items.isEmpty()) {
                // 未読込の場合だけプログレスバーを表示
                _uiState.update { it.copy(isLoading = true) }
            }
            useCase(pm).onSuccess { res ->
                _uiState.update { it.copy(items = res.targets) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    /** メッセージ受信 */
    fun messageReceived(message: Message) {
        _uiState.update { it.copy(message = message) }
    }

    /** メッセージを表示した */
    fun messageShown() {
        _uiState.update { it.copy(message = null) }
    }
}