package me.nya_n.notificationnotifier.ui.screen.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.nya_n.notificationnotifier.domain.usecase.CheckPackageVisibilityUseCase
import me.nya_n.notificationnotifier.domain.usecase.PackageVisibilityGrantedUseCase

class AppViewModel(
    private val isPackageVisibilityGranted: CheckPackageVisibilityUseCase,
    private val packageVisibilityGrantedUseCase: PackageVisibilityGrantedUseCase
) : ViewModel() {

    /** UIの状態 */
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun checkPermissions() {
        _uiState.update {
            it.copy(
                isShowRequirePackageVisibilityDialog = !isPackageVisibilityGranted()
            )
        }
    }

    fun onPackageVisibilityGranted() {
        packageVisibilityGrantedUseCase()
        checkPermissions()
    }
}