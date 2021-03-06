package me.nya_n.notificationnotifier.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectionViewModel(
) : ViewModel() {
    val query = MutableLiveData("")
}