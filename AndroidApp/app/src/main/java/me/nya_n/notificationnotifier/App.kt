package me.nya_n.notificationnotifier

import android.app.Application
import me.nya_n.notificationnotifier.domain.usecase.*
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.repositories.sources.DB
import me.nya_n.notificationnotifier.views.screen.MainViewModel
import me.nya_n.notificationnotifier.views.screen.SharedViewModel
import me.nya_n.notificationnotifier.views.screen.detail.DetailViewModel
import me.nya_n.notificationnotifier.views.screen.selection.SelectionViewModel
import me.nya_n.notificationnotifier.views.screen.top.TopViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(modules)
        }
    }

    private val modules = module {
        // DataStore
        single { DB.get(get()).filterConditionDao() }
        single { DB.get(get()).targetAppDao() }

        // Repository
        single { UserSettingRepository(get()) }
        single { AppRepository(get(), get()) }

        // ViewModel
        viewModel { MainViewModel(get(), get()) }
        viewModel { SharedViewModel(get(), get(), get()) }
        viewModel { TopViewModel(get(), get(), get()) }
        viewModel { SelectionViewModel(get()) }
        viewModel { params -> DetailViewModel(get(), get(), get(), get(), params.get()) }

        // UseCase
        factory { AddTargetAppUseCase(get()) }
        factory { DeleteTargetAppUseCase(get()) }
        factory { ExportDataUseCase(get(), get()) }
        factory { ImportDataUseCase(get(), get()) }
        factory { LoadAddressUseCase(get()) }
        factory { LoadAppUseCase(get(), get()) }
        factory { LoadFilterConditionUseCase(get()) }
        factory { NotifyTestUseCase(get()) }
        factory { PackageVisibilityGrantedUseCase(get()) }
        factory { SaveAddressUseCase(get()) }
        factory { SaveFilterConditionUseCase(get()) }
    }
}