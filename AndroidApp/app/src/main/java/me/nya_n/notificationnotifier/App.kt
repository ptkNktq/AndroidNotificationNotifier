package me.nya_n.notificationnotifier

import android.app.Application
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingRepository
import me.nya_n.notificationnotifier.data.repository.impl.AppRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.impl.UserSettingRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.data.repository.source.UserSettingDataStore
import me.nya_n.notificationnotifier.domain.usecase.AddTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.DeleteTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.ExportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.ImportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotificationUseCase
import me.nya_n.notificationnotifier.domain.usecase.PackageVisibilityGrantedUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.util.SharedPreferenceProvider
import me.nya_n.notificationnotifier.ui.screen.detail.DetailViewModel
import me.nya_n.notificationnotifier.ui.screen.selection.SelectionViewModel
import me.nya_n.notificationnotifier.ui.screen.setting.SettingViewModel
import me.nya_n.notificationnotifier.ui.screen.target.TargetViewModel
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
        single {
            UserSettingDataStore(
                SharedPreferenceProvider.create(
                    get(),
                    UserSettingDataStore.DATA_STORE_NAME
                )
            )
        }
        single { DB.get(get()).filterConditionDao() }
        single { DB.get(get()).targetAppDao() }

        // Repository
        single<UserSettingRepository> { UserSettingRepositoryImpl(get()) }
        single<AppRepository> { AppRepositoryImpl(get(), get()) }

        // ViewModel
        viewModel { SelectionViewModel(get(), get(), get()) }
        viewModel { params -> DetailViewModel(get(), get(), get(), params.get()) }
        viewModel { TargetViewModel(get(), get()) }
        viewModel { SettingViewModel(get(), get(), get(), get(), get()) }

        // UseCase
        factory { AddTargetAppUseCase(get()) }
        factory { DeleteTargetAppUseCase(get()) }
        factory { ExportDataUseCase(get(), get()) }
        factory { ImportDataUseCase(get(), get()) }
        factory { LoadAddressUseCase(get()) }
        factory { LoadAppUseCase(get(), get()) }
        factory { LoadFilterConditionUseCase(get()) }
        factory { NotificationUseCase(get()) }
        factory { PackageVisibilityGrantedUseCase(get()) }
        factory { SaveAddressUseCase(get()) }
        factory { SaveFilterConditionUseCase(get()) }
    }
}