package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.source.UserSettingsDataStore
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase

class LoadAddressUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository
) : LoadAddressUseCase {
    override operator fun invoke(): String {
        val settings = userSettingsRepository.getUserSettings()
        val port = if (settings.port == UserSettingsDataStore.DEFAULT_PORT) {
            ""
        } else {
            settings.port
        }
        val addr = "${settings.host}:${port}"
        return if (addr.length == 1) "" else addr
    }
}