package me.nya_n.notificationnotifier

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingRepository
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.data.repository.source.UserSettingDataStore
import me.nya_n.notificationnotifier.domain.usecase.*
import me.nya_n.notificationnotifier.domain.util.SharedPreferenceProvider
import me.nya_n.notificationnotifier.model.InstalledApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@Suppress("NonAsciiCharacters", "RemoveRedundantBackticks")
@RunWith(AndroidJUnit4::class)
class UseCaseTest {
    private lateinit var appContext: Context
    private lateinit var userSettingRepository: UserSettingRepository
    private lateinit var appRepository: AppRepository
    private lateinit var pm: PackageManager
    private lateinit var exportFile: File
    private val exportFileName: String = "export.json"

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        exportFile = appContext.filesDir
        File(exportFile, exportFileName).apply {
            if (exists()) {
                delete()
            }
        }
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
            isInstanceOf(me.nya_n.notificationnotifier.model.AppException.PermissionDeniedException::class.java)
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
            assertThat(
                NotifyUseCase(userSettingRepository)("通知テスト").exceptionOrNull()
            ).isNotNull()
        }
    }

    @Test
    fun `通知送信_成功`() {
        runBlocking {
            val host = "192.168.11.4"
            val port = 5555
            val addr = "$host:$port"
            SaveAddressUseCase(userSettingRepository)(addr)
            assertThat(
                NotifyUseCase(userSettingRepository)("通知テスト").getOrNull()
            ).isNotNull()
        }
    }

    @Test
    fun `バックアップ、復元`() {
        val uri = Uri.fromFile(File.createTempFile(exportFileName, null, exportFile))
        runBlocking {
            val targetSaver = AddTargetAppUseCase(appRepository)
            val condSaver = SaveFilterConditionUseCase(appRepository)
            val addrSaver = SaveAddressUseCase(userSettingRepository)

            // 初期値の保存
            // ターゲット
            val app = InstalledApp("export", "test.export")
            targetSaver(app)
            // 条件
            val cond = ".*"
            condSaver(SaveFilterConditionUseCase.Args(app, cond))
            // アドレス
            val addr = "192.168.1.4:5050"
            addrSaver(addr)

            // バックアップ
            ExportDataUseCase(userSettingRepository, appRepository)(appContext, uri)

            // バックアップ時とは異なるように適当に変更
            // ターゲット
            targetSaver(InstalledApp("new", "new"))
            // 条件
            condSaver(SaveFilterConditionUseCase.Args(app, "new"))

            // 復元
            ImportDataUseCase(userSettingRepository, appRepository)(appContext, uri)

            // 正常に復元できているか確認
            // ターゲット一覧
            val restoreTargets =
                LoadAppUseCase(userSettingRepository, appRepository).loadTargetList()
            assertThat(restoreTargets).apply {
                hasSize(1)
                contains(app)
            }
            // 条件
            val restoreCond = LoadFilterConditionUseCase(appRepository)(app)
            assertThat(restoreCond).isEqualTo(cond)
            // アドレス
            val restoreAddr = LoadAddressUseCase(userSettingRepository)()
            assertThat(restoreAddr).isEqualTo(addr)
        }
    }
}