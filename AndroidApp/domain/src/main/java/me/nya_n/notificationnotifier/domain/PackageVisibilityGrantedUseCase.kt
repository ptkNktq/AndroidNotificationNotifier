package me.nya_n.notificationnotifier.domain

import me.nya_n.notificationnotifier.data.repository.UserSettingRepository

class PackageVisibilityGrantedUseCase(
    private val userSettingRepository: UserSettingRepository
) {
    operator fun invoke() {
        val setting = userSettingRepository.getUserSetting()
            .copy(isPackageVisibilityGranted = true)
        userSettingRepository.saveUserSetting(setting)
    }
}