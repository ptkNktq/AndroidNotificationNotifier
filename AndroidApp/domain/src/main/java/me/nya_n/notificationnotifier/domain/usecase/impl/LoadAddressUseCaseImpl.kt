package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingRepository
import me.nya_n.notificationnotifier.data.repository.source.UserSettingDataStore
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase

class LoadAddressUseCaseImpl(
    private val userSettingRepository: UserSettingRepository
) : LoadAddressUseCase {
    override operator fun invoke(): String {
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