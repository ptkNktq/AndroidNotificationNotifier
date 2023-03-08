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

class TargetViewModel(
    context: Context,
    private val useCase: LoadAppUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCase(context.packageManager).onSuccess { res ->
                _uiState.update { it.copy(items = res.targets) }
            }
        }
    }
}