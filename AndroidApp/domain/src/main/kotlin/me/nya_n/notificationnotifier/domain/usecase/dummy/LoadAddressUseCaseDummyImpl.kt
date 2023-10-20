package me.nya_n.notificationnotifier.domain.usecase.dummy

import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase

class LoadAddressUseCaseDummyImpl : LoadAddressUseCase {
    override fun invoke(): String {
        return "192.168.1.2:5555"
    }
}