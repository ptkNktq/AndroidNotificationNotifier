package me.nya_n.notificationnotifier.domain.usecase.impl

import androidx.core.text.isDigitsOnly
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.model.AppException

class SaveAddressUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository
) : SaveAddressUseCase {
    override operator fun invoke(address: String?): Result<Unit> {
        val addr = (address ?: "").split(":")
        if (addr.size != 2
            || addr[0].isEmpty()
            || !(addr[1].isNotEmpty() && addr[1].isDigitsOnly())
        ) {
            return Result.failure(AppException.InvalidAddrException())
        }
        val settings = userSettingsRepository.getUserSettings()
            .copy(
                host = addr[0],
                port = addr[1].toInt()
            )
        userSettingsRepository.saveUserSettings(settings)
        return Result.success(Unit)
    }
}