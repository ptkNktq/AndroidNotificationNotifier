package me.nya_n.notificationnotifier.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.nya_n.notificationnotifier.entities.Fab
import me.nya_n.notificationnotifier.utils.Event

class MainViewModel : ViewModel() {
    val fab = MutableLiveData<Event<Fab>>()
}