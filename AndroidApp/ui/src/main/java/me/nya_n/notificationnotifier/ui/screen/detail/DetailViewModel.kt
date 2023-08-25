package me.nya_n.notificationnotifier.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.domain.usecase.DeleteTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase
import me.nya_n.notificationnotifier.model.InstalledApp

class DetailViewModel(
    private val loadFilterConditionUseCase: LoadFilterConditionUseCase,
    private val saveFilterConditionUseCase: SaveFilterConditionUseCase,
    private val deleteTargetAppUseCase: DeleteTargetAppUseCase,
    private val target: InstalledApp
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        /**
         * 通知条件を読み込む
         */
        viewModelScope.launch {
            _uiState.update { it.copy(condition = loadFilterConditionUseCase(target)) }
        }
    }

    /**
     * 選択したアプリを通知対象から外す
     */
    fun deleteTarget() {
        viewModelScope.launch {
            deleteTargetAppUseCase(target)
        }
    }

    /**
     * 通知条件を保存
     */
    fun save(condition: String) {
        viewModelScope.launch {
            saveFilterConditionUseCase(
                SaveFilterConditionUseCase.Args(target, condition)
            )
            _uiState.update { it.copy(condition = condition) }
        }
    }

    /**
     * メッセージを表示した
     */
    fun messageShown() {
        _uiState.update { it.copy(message = null) }
    }
}