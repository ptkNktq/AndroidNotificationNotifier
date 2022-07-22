package me.nya_n.notificationnotifier.views.screen.detail

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.domain.entities.Message
import me.nya_n.notificationnotifier.domain.usecase.DeleteTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase
import me.nya_n.notificationnotifier.utils.AppIcon
import me.nya_n.notificationnotifier.utils.Event

class DetailViewModel(
    context: Context,
    private val loadFilterConditionUseCase: LoadFilterConditionUseCase,
    private val saveFilterConditionUseCase: SaveFilterConditionUseCase,
    private val deleteTargetAppUseCase: DeleteTargetAppUseCase,
    private val target: InstalledApp
) : ViewModel() {
    val targetIcon = liveData {
        emit(AppIcon.get(target.packageName, context.packageManager))
    }
    val targetName = liveData {
        emit(target.label)
    }
    private val _message = MutableLiveData<Event<Message>>()
    val message: LiveData<Event<Message>> = _message
    private val _targetDeleted = MutableLiveData<Event<InstalledApp>>()
    val targetDeleted: LiveData<Event<InstalledApp>> = _targetDeleted
    val filterCondition = MutableLiveData<String>()

    init {
        /**
         * 通知条件を読み込む
         */
        viewModelScope.launch {
            val cond = loadFilterConditionUseCase(target)
            filterCondition.postValue(cond)
        }
    }

    /**
     * 選択したアプリを通知対象から外す
     */
    fun deleteTarget() {
        viewModelScope.launch {
            deleteTargetAppUseCase(target)
            _message.postValue(Event(Message.Notice(R.string.deleted)))
            _targetDeleted.postValue(Event(target))
        }
    }

    /**
     * 通知条件を保存
     */
    fun save() {
        viewModelScope.launch {
            saveFilterConditionUseCase(
                SaveFilterConditionUseCase.Args(target, filterCondition.value)
            )
            _message.postValue(Event(Message.Notice(R.string.condition_added)))
        }
    }
}