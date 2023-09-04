package me.nya_n.notificationnotifier.ui.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class AppViewModel<UiEvent : AppUiEvent> : ViewModel() {
    /**
     * 単発のイベント
     *  - イベント消化後はAppViewModel#consumeEventを呼ぶ
     */
    private val _uiEvent = MutableStateFlow(emptyList<UiEvent>())
    val uiEvent: StateFlow<List<UiEvent>> = _uiEvent.asStateFlow()

    /**
     * イベント発生
     */
    fun event(uiEvent: UiEvent) {
        _uiEvent.update { it + listOf(uiEvent) }
    }

    /**
     * イベント消化
     */
    fun consumeEvent(uiEvent: UiEvent) {
        _uiEvent.update { it.filterNot { e -> e == uiEvent } }
    }
}