package me.nya_n.notificationnotifier.domain.usecase.dummy

import android.content.pm.PackageManager
import me.nya_n.notificationnotifier.domain.usecase.LoadAppUseCase
import me.nya_n.notificationnotifier.model.InstalledApp

class LoadAppUseCaseDummyImpl : LoadAppUseCase {
    override suspend fun invoke(pm: PackageManager): Result<LoadAppUseCase.Outputs> {
        return Result.success(
            LoadAppUseCase.Outputs(
                listOf(
                    InstalledApp("サンプル アプリ１", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ２", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ３", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ４", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ５", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ６", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ７", "me.nya_n.notificationnotifier"),
                ),
                listOf(
                    InstalledApp("サンプル アプリ１", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ２", "me.nya_n.notificationnotifier"),
                    InstalledApp("サンプル アプリ７", "me.nya_n.notificationnotifier"),
                )
            )
        )
    }
}