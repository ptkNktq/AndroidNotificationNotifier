package me.nya_n.notificationnotifier.domain.usecase

import androidx.core.text.isDigitsOnly
import me.nya_n.notificationnotifier.model.AppException
import me.nya_n.notificationnotifier.repository.UserSettingRepository

/**
 * IPアドレスを保存する
 */
class SaveAddressUseCase(
    private val userSettingRepository: UserSettingRepository
) {
    operator fun invoke(address: String?): Result<Unit> {
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