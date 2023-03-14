package me.nya_n.notificationnotifier.ui.screen.selection

import android.content.Context
import android.content.pm.PackageManager
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

class SelectionViewModel(
    context: Context,
    private val loadAppUseCase: LoadAppUseCase,
    private val addTargetAppUseCase: AddTargetAppUseCase
) : ViewModel() {
    private val pm: PackageManager = context.packageManager

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadAppList()
    }

    /**
     * アプリ一覧の読み込み
     */
    fun loadAppList() {
        viewModelScope.launch {
            loadAppUseCase(pm).onSuccess { res ->
                _uiState.update { it.copy(items = res.installs) }
            }
        }
    }

    /**
     * 通知送信対象に追加
     */
    fun addTarget(target: InstalledApp) {
        viewModelScope.launch {
            addTargetAppUseCase(target)
            _uiState.update { it.copy(message = Message.Notice(R.string.added)) }
        }
    }

    /**
     * 条件に従ってアプリ検索
     * @param query 検索条件
     */
    fun searchApp(query: String) {
        viewModelScope.launch {
            loadAppUseCase(pm).onSuccess { res ->
                // ラベルもしくはパッケージとの部分一致のみ
                val items = res.installs
                    .filter { app -> app.label.contains(query) || app.packageName.contains(query) }
                _uiState.update {
                    it.copy(
                        items = items,
                        query = query
                    )
                }
            }
        }
    }

    /**
     * メッセージを表示した
     */
    fun messageShown() {
        _uiState.update { it.copy(message = null) }
    }
}