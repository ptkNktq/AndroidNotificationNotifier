package me.nya_n.notificationnotifier

import android.app.Application
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.impl.AppRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.impl.UserSettingsRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.data.repository.source.UserSettingsDataStore
import me.nya_n.notificationnotifier.domain.usecase.AddTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.CheckPackageVisibilityUseCase
import me.nya_n.notificationnotifier.domain.usecase.DeleteTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.ExportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.ImportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotifyTargetAppNotificationUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase
import me.nya_n.notificationnotifier.domain.usecase.PackageVisibilityGrantedUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.impl.AddTargetAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.CheckPackageVisibilityUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.DeleteTargetAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ExportDataUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ImportDataUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadAddressUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadFilterConditionUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.NotifyTargetAppNotificationUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.NotifyUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.PackageVisibilityGrantedUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.SaveAddressUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.SaveFilterConditionUseCaseImpl
import me.nya_n.notificationnotifier.domain.util.SharedPreferenceProvider
import me.nya_n.notificationnotifier.ui.screen.app.AppViewModel
import me.nya_n.notificationnotifier.ui.screen.detail.DetailViewModel
import me.nya_n.notificationnotifier.ui.screen.selection.SelectionViewModel
import me.nya_n.notificationnotifier.ui.screen.settings.SettingsViewModel
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
            UserSettingsDataStore(
                SharedPreferenceProvider.create(
                    get(),
                    UserSettingsDataStore.DATA_STORE_NAME
                )
            )
        }
        single { DB.get(get()).filterConditionDao() }
        single { DB.get(get()).targetAppDao() }

        // Repository
        single<UserSettingsRepository> { UserSettingsRepositoryImpl(get()) }
        single<AppRepository> { AppRepositoryImpl(get(), get()) }

        // ViewModel
        viewModel { AppViewModel(get(), packageName, get(), get()) }
        viewModel { SelectionViewModel(get(), get(), get()) }
        viewModel { params -> DetailViewModel(get(), get(), get(), params.get()) }
        viewModel { TargetViewModel(get(), get()) }
        viewModel { SettingsViewModel(get(), get(), get(), get(), get()) }

        // UseCase
        factory<AddTargetAppUseCase> { AddTargetAppUseCaseImpl(get()) }
        factory<DeleteTargetAppUseCase> { DeleteTargetAppUseCaseImpl(get()) }
        factory<ExportDataUseCase> { ExportDataUseCaseImpl(get(), get()) }
        factory<ImportDataUseCase> { ImportDataUseCaseImpl(get(), get()) }
        factory<LoadAddressUseCase> { LoadAddressUseCaseImpl(get()) }
        factory<LoadAppUseCase> { LoadAppUseCaseImpl(get(), get()) }
        factory<LoadFilterConditionUseCase> { LoadFilterConditionUseCaseImpl(get()) }
        factory<NotifyTargetAppNotificationUseCase> {
            NotifyTargetAppNotificationUseCaseImpl(get(), get())
        }
        factory<NotifyUseCase> { NotifyUseCaseImpl(get()) }
        factory<PackageVisibilityGrantedUseCase> { PackageVisibilityGrantedUseCaseImpl(get()) }
        factory<CheckPackageVisibilityUseCase> { CheckPackageVisibilityUseCaseImpl(get()) }
        factory<SaveAddressUseCase> { SaveAddressUseCaseImpl(get()) }
        factory<SaveFilterConditionUseCase> { SaveFilterConditionUseCaseImpl(get()) }
    }
}