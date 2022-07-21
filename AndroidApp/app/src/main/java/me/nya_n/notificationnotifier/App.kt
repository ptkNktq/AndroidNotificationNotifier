package me.nya_n.notificationnotifier

import android.app.Application
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
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
        // Repository
        single { UserSettingRepository(get()) }
        single { AppRepository(get()) }

        // ViewModel
        viewModel { MainViewModel(get(), get()) }
        viewModel { SharedViewModel(get(), get(), get()) }
        viewModel { TopViewModel(get()) }
        viewModel { SelectionViewModel(get()) }
        viewModel { params -> DetailViewModel(get(), get(), params.get()) }
    }
}