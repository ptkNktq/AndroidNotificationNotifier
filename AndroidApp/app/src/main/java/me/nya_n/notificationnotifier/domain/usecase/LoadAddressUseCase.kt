package me.nya_n.notificationnotifier.domain.usecase

import me.nya_n.notificationnotifier.repository.UserSettingRepository
import me.nya_n.notificationnotifier.repository.source.UserSettingDataStore

/**
 * ユーザー設定からIPアドレスを取得する
 */
class LoadAddressUseCase(
    private val userSettingRepository: UserSettingRepository
) {
    operator fun invoke(): String {
        val setting = userSettingRepository.getUserSetting()
        val port = if (setting.port == UserSettingDataStore.DEFAULT_PORT) {
            ""
        } else {
            setting.port
        }
        val addr = "${setting.host}:${port}"
        return if (addr.length == 1) "" else addr
    }
}