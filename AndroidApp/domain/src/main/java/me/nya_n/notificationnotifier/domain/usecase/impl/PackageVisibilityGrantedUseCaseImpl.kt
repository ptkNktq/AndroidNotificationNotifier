package me.nya_n.notificationnotifier.domain.usecase.impl

import me.nya_n.notificationnotifier.data.repository.UserSettingRepository
import me.nya_n.notificationnotifier.domain.usecase.PackageVisibilityGrantedUseCase

class PackageVisibilityGrantedUseCaseImpl(
    private val userSettingRepository: UserSettingRepository
) : PackageVisibilityGrantedUseCase {
    override operator fun invoke() {
        val setting = userSettingRepository.getUserSetting()
            .copy(isPackageVisibilityGranted = true)
        userSettingRepository.saveUserSetting(setting)
    }
}