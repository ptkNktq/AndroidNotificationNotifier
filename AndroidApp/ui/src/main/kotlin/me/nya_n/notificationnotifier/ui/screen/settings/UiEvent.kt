package me.nya_n.notificationnotifier.ui.screen.settings

import me.nya_n.notificationnotifier.ui.screen.BaseUiEvent

sealed class UiEvent : BaseUiEvent() {
    /** バックアップのために外部ストレージにデータを保存 */
    class ExportData : UiEvent()

    /** 外部ストレージのバックアップからデータを復元 */
    class ImportData : UiEvent()
}