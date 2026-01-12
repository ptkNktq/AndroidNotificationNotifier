package me.nya_n.notificationnotifier

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.impl.AppRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.impl.UserSettingsRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.data.repository.source.UserSettingsDataStore
import me.nya_n.notificationnotifier.data.repository.util.SharedPreferenceProvider
import me.nya_n.notificationnotifier.domain.usecase.AddTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.DeleteTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.ToggleIgnoreSummaryUseCase
import me.nya_n.notificationnotifier.domain.usecase.impl.AddTargetAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.DeleteTargetAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ExportDataUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ImportDataUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadAddressUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadFilterConditionUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.NotifyUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.PackageVisibilityGrantedUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.SaveAddressUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.SaveFilterConditionUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ToggleIgnoreSummaryUseCaseImpl
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@Suppress("NonAsciiCharacters", "RemoveRedundantBackticks")
@RunWith(AndroidJUnit4::class)
class UseCaseTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var appContext: Context
    private lateinit var userSettingsRepository: UserSettingsRepository
    private lateinit var appRepository: AppRepository
    private lateinit var addTargetAppUseCase: AddTargetAppUseCase
    private lateinit var loadAppUseCase: LoadAppUseCaseImpl
    private lateinit var deleteTargetAppUseCase: DeleteTargetAppUseCase
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
        userSettingsRepository = UserSettingsRepositoryImpl(
            UserSettingsDataStore(
                SharedPreferenceProvider.create(
                    appContext,
                    UserSettingsDataStore.DATA_STORE_NAME
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
        appRepository = AppRepositoryImpl(
            db.filterConditionDao(),
            db.targetAppDao(),
            testDispatcher
        )
        addTargetAppUseCase = AddTargetAppUseCaseImpl(appRepository)
        loadAppUseCase = LoadAppUseCaseImpl(userSettingsRepository, appRepository)
        deleteTargetAppUseCase = DeleteTargetAppUseCaseImpl(appRepository)
    }

    @Test
    fun `通知対象アプリの追加、取得、削除`() {
        runTest(testDispatcher) {
            val app = InstalledApp("sample", "com.sample.www")
            addTargetAppUseCase(app)

            val added = loadAppUseCase.loadTargetList()
            assertThat(added).hasSize(1)
            assertThat(added.first()).isEqualTo(app)

            deleteTargetAppUseCase(app)
            val deleted = loadAppUseCase.loadTargetList()
            assertThat(deleted).isEmpty()
        }
    }

    @Test
    fun `インストール済みアプリの取得_成功（ついでにアプリ一覧取得権限許可処理も）`() {
        PackageVisibilityGrantedUseCaseImpl(userSettingsRepository)()
        val ret = loadAppUseCase.loadInstalledAppList(pm)
        assertThat(ret.getOrNull()).apply {
            isNotNull()
            isNotEmpty()
        }
    }

    @Test
    fun `インストール済みアプリの取得_失敗`() {
        val ret = loadAppUseCase.loadInstalledAppList(pm)
        assertThat(ret.exceptionOrNull()).apply {
            isNotNull()
            isInstanceOf(me.nya_n.notificationnotifier.model.AppException.PermissionDeniedException::class.java)
        }
    }

    @Test
    fun `通知条件の追加、取得、更新`() {
        runTest(testDispatcher) {
            val cond = "test"
            val updatedCond = "updated"
            val packageName = "com.sample.www"
            val app = InstalledApp("sample", packageName)

            val saver = SaveFilterConditionUseCaseImpl(appRepository)
            val toggler = ToggleIgnoreSummaryUseCaseImpl(appRepository)
            val loader = LoadFilterConditionUseCaseImpl(appRepository)

            // 追加
            saver(SaveFilterConditionUseCase.Args(app, cond))
            assertThat(loader(app)).isEqualTo(FilterCondition(packageName, false, cond))

            // メッセージ条件の更新
            saver(SaveFilterConditionUseCase.Args(app, updatedCond))
            assertThat(loader(app)).isEqualTo(FilterCondition(packageName, false, updatedCond))

            // サマリー条件の更新
            toggler.invoke(ToggleIgnoreSummaryUseCase.Args(app))
            assertThat(loader(app)).isEqualTo(FilterCondition(packageName, true, updatedCond))
        }
    }

    @Test
    fun `IPアドレスの追加、更新_成功、成功`() {
        val saver = SaveAddressUseCaseImpl(userSettingsRepository)
        val loader = LoadAddressUseCaseImpl(userSettingsRepository)

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
        val saver = SaveAddressUseCaseImpl(userSettingsRepository)
        val loader = LoadAddressUseCaseImpl(userSettingsRepository)

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
        assertThat(SaveAddressUseCaseImpl(userSettingsRepository)(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portなし`() {
        val host = "192.168.11.4"
        val addr = "$host:"
        assertThat(SaveAddressUseCaseImpl(userSettingsRepository)(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portが数値じゃない`() {
        val host = "192.168.11.4"
        val addr = "$host:test"
        assertThat(SaveAddressUseCaseImpl(userSettingsRepository)(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `通知送信_失敗`() {
        runTest(testDispatcher) {
            assertThat(
                NotifyUseCaseImpl(
                    userSettingsRepository,
                    testDispatcher
                )("通知テスト").exceptionOrNull()
            ).isNotNull()
        }
    }

    @Test
    @Ignore("FIXME: socket failed: EPERM (Operation not permitted)")
    fun `通知送信_成功`() {
        runTest(testDispatcher) {
            val host = "192.168.11.4"
            val port = 5555
            val addr = "$host:$port"
            SaveAddressUseCaseImpl(userSettingsRepository)(addr)
            assertThat(
                NotifyUseCaseImpl(userSettingsRepository, testDispatcher)("通知テスト").getOrNull()
            ).isNotNull()
        }
    }

    @Test
    fun `バックアップ、復元`() {
        val uri = Uri.fromFile(File.createTempFile(exportFileName, null, exportFile))
        runTest(testDispatcher) {
            val condSaver = SaveFilterConditionUseCaseImpl(appRepository)
            val addrSaver = SaveAddressUseCaseImpl(userSettingsRepository)
            val toggler = ToggleIgnoreSummaryUseCaseImpl(appRepository)

            // 初期値の保存
            // ターゲット
            val packageName = "test.export"
            val app = InstalledApp("export", packageName)
            addTargetAppUseCase(app)
            // 条件
            val cond = ".*"
            condSaver(SaveFilterConditionUseCase.Args(app, cond))
            toggler.invoke(ToggleIgnoreSummaryUseCase.Args(app))
            // アドレス
            val addr = "192.168.1.4:5050"
            addrSaver(addr)

            // バックアップ
            ExportDataUseCaseImpl(userSettingsRepository, appRepository, testDispatcher)(
                appContext,
                uri
            )

            // バックアップ時とは異なるように適当に変更
            // ターゲット
            addTargetAppUseCase(InstalledApp("new", "new"))
            // 条件
            condSaver(SaveFilterConditionUseCase.Args(app, "new"))
            toggler.invoke(ToggleIgnoreSummaryUseCase.Args(app))

            // 復元
            ImportDataUseCaseImpl(userSettingsRepository, appRepository, testDispatcher)(
                appContext,
                uri
            )

            // 正常に復元できているか確認
            // ターゲット一覧
            val restoreTargets = loadAppUseCase.loadTargetList()
            assertThat(restoreTargets).apply {
                hasSize(1)
                contains(app)
            }
            // 条件
            val restoreCond = LoadFilterConditionUseCaseImpl(appRepository)(app)
            assertThat(restoreCond).isEqualTo(FilterCondition(packageName, true, cond))
            // アドレス
            val restoreAddr = LoadAddressUseCaseImpl(userSettingsRepository)()
            assertThat(restoreAddr).isEqualTo(addr)
        }
    }
}