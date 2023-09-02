package me.nya_n.notificationnotifier.domain.usecase.impl

import androidx.core.text.isDigitsOnly
import me.nya_n.notificationnotifier.data.repository.UserSettingRepository
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.model.AppException

class SaveAddressUseCaseImpl(
    private val userSettingRepository: UserSettingRepository
) : SaveAddressUseCase {
    override operator fun invoke(address: String?): Result<Unit> {
        val addr = (address ?: "").split(":")
        if (addr.size != 2
            || addr[0].isEmpty()
            || !(addr[1].isNotEmpty() && addr[1].isDigitsOnly())
        ) {
            return Result.failure(AppException.InvalidAddrException())
        }
        val setting = userSettingRepository.getUserSetting()
            .copy(
                host = addr[0],
                port = addr[1].toInt()
            )
        userSettingRepository.saveUserSetting(setting)
        return Result.success(Unit)
    }
}