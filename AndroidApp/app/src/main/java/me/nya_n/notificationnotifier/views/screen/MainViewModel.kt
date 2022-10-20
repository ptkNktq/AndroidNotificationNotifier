package me.nya_n.notificationnotifier.views.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.nya_n.notificationnotifier.model.Fab
import me.nya_n.notificationnotifier.utils.Event

class MainViewModel : ViewModel() {
    private val _fab = MutableLiveData<Event<Fab>>()
    val fab: LiveData<Event<Fab>> = _fab

    /**
     * Fabの状態を更新
     */
    fun changeFabState(fab: Fab) {
        _fab.postValue(Event(fab))
    }
}