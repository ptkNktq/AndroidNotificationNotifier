package me.nya_n.notificationnotifier

import android.content.pm.PackageManager
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import me.nya_n.notificationnotifier.domain.entities.AppException
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.domain.usecase.*
import me.nya_n.notificationnotifier.repositories.AppRepository
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import me.nya_n.notificationnotifier.repositories.sources.DB
import me.nya_n.notificationnotifier.repositories.sources.UserSettingDataStore
import me.nya_n.notificationnotifier.utils.SharedPreferenceProvider
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UseCaseTest {

    private lateinit var userSettingRepository: UserSettingRepository
    private lateinit var appRepository: AppRepository
    private lateinit var pm: PackageManager

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        pm = appContext.packageManager
        userSettingRepository = UserSettingRepository(
            UserSettingDataStore(
                SharedPreferenceProvider.create(
                    appContext,
                    UserSettingDataStore.DATA_STORE_NAME
                ).apply {
                    edit {
                        clear()
                    }
                }
            )
        )
        val db = DB.get(appContext, true).apply {
            clearAllTables()
        }
        appRepository = AppRepository(
            db.filterConditionDao(),
            db.targetAppDao()
        )
    }

    @Test
    fun `通知対象アプリの追加、取得、削除`() {
        runBlocking {
            val app = InstalledApp("sample", "com.sample.www")
            AddTargetAppUseCase(appRepository)(app)

            val loader = LoadAppUseCase(userSettingRepository, appRepository)
            val added = loader.loadTargetList()
            assertThat(added).hasSize(1)
            assertThat(added.first()).isEqualTo(app)

            DeleteTargetAppUseCase(appRepository)(app)
            val deleted = loader.loadTargetList()
            assertThat(deleted).isEmpty()
        }
    }

    @Test
    fun `インストール済みアプリの取得_成功（ついでにアプリ一覧取得権限許可処理も）`() {
        PackageVisibilityGrantedUseCase(userSettingRepository)()
        val ret = LoadAppUseCase(userSettingRepository, appRepository).loadInstalledAppList(pm)
        assertThat(ret.getOrNull()).apply {
            isNotNull()
            isNotEmpty()
        }
    }

    @Test
    fun `インストール済みアプリの取得_失敗`() {
        val ret = LoadAppUseCase(userSettingRepository, appRepository).loadInstalledAppList(pm)
        assertThat(ret.exceptionOrNull()).apply {
            isNotNull()
            isInstanceOf(AppException.PermissionDeniedException::class.java)
        }
    }

    @Test
    fun `通知条件の追加、取得、更新`() {
        runBlocking {
            val cond = "test"
            val updatedCond = "updated"
            val app = InstalledApp("sample", "com.sample.www")
            val saver = SaveFilterConditionUseCase(appRepository)
            saver(SaveFilterConditionUseCase.Args(app, cond))

            val loader = LoadFilterConditionUseCase(appRepository)
            assertThat(loader(app)).isEqualTo(cond)

            saver(SaveFilterConditionUseCase.Args(app, updatedCond))
            assertThat(loader(app)).isEqualTo(updatedCond)
        }
    }

    @Test
    fun `IPアドレスの追加、更新_成功、成功`() {
        val saver = SaveAddressUseCase(userSettingRepository)
        val loader = LoadAddressUseCase(userSettingRepository)

        val host = "192.168.11.4"
        val port = 5555
        val addr = "$host:$port"
        assertThat(saver(addr).getOrNull()).isNotNull()
        assertThat(loader()).isEqualTo(addr)

        val updatedHost = "192.168.11.2"
        val updatedPort = 3456
        val updatedAddr = "$updatedHost:$updatedPort"
        assertThat(saver(updatedAddr).getOrNull()).isNotNull()
        assertThat(loader()).isEqualTo(updatedAddr)
    }

    @Test
    fun `IPアドレスの追加、更新_成功、失敗`() {
        val saver = SaveAddressUseCase(userSettingRepository)
        val loader = LoadAddressUseCase(userSettingRepository)

        val host = "192.168.11.4"
        val port = 5555
        val addr = "$host:$port"
        assertThat(saver(addr).getOrNull()).isNotNull()
        assertThat(loader()).isEqualTo(addr)

        val updatedHost = "192.168.11.2"
        val updatedAddr = "$updatedHost:"
        assertThat(saver(updatedAddr).exceptionOrNull()).isNotNull()
        assertThat(loader()).isEqualTo(addr)
    }

    @Test
    fun `IPアドレスの追加_失敗_hostなし`() {
        val port = 5555
        val addr = ":$port"
        assertThat(SaveAddressUseCase(userSettingRepository)(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portなし`() {
        val host = "192.168.11.4"
        val addr = "$host:"
        assertThat(SaveAddressUseCase(userSettingRepository)(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portが数値じゃない`() {
        val host = "192.168.11.4"
        val addr = "$host:test"
        assertThat(SaveAddressUseCase(userSettingRepository)(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `通知送信_失敗`() {
        runBlocking {
            assertThat(NotifyTestUseCase(userSettingRepository)().exceptionOrNull()).isNotNull()
        }
    }

    @Test
    fun `通知送信_成功`() {
        runBlocking {
            val host = "192.168.11.4"
            val port = 5555
            val addr = "$host:$port"
            SaveAddressUseCase(userSettingRepository)(addr)
            assertThat(NotifyTestUseCase(userSettingRepository)().getOrNull()).isNotNull()
        }
    }

    @Ignore("どんな感じにするか悩み中")
    @Test
    fun `バックアップ、復元`() {
    }
}