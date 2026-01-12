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
import me.nya_n.notificationnotifier.data.repository.impl.AppRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.impl.UserSettingsRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.data.repository.source.UserSettingsDataStore
import me.nya_n.notificationnotifier.data.repository.util.SharedPreferenceProvider
import me.nya_n.notificationnotifier.domain.usecase.AddTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.DeleteTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.ExportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.ImportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase
import me.nya_n.notificationnotifier.domain.usecase.PackageVisibilityGrantedUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
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
import me.nya_n.notificationnotifier.model.AppException.PermissionDeniedException
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
    companion object {
        private const val ExportFileName: String = "export.json"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var appContext: Context
    private lateinit var pm: PackageManager
    private lateinit var exportFile: File

    private lateinit var addTargetAppUseCase: AddTargetAppUseCase
    private lateinit var loadAppUseCase: LoadAppUseCaseImpl
    private lateinit var deleteTargetAppUseCase: DeleteTargetAppUseCase
    private lateinit var packageVisibilityGrantedUseCase: PackageVisibilityGrantedUseCase
    private lateinit var saveFilterConditionUseCase: SaveFilterConditionUseCase
    private lateinit var toggleIgnoreSummaryUseCase: ToggleIgnoreSummaryUseCase
    private lateinit var loadFilterConditionUseCase: LoadFilterConditionUseCase
    private lateinit var saveAddressUseCase: SaveAddressUseCase
    private lateinit var loadAddressUseCase: LoadAddressUseCase
    private lateinit var notifyUseCase: NotifyUseCase
    private lateinit var exportDataUseCase: ExportDataUseCase
    private lateinit var importDataUseCase: ImportDataUseCase

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        exportFile = appContext.filesDir
        File(exportFile, ExportFileName).apply {
            if (exists()) {
                delete()
            }
        }
        pm = appContext.packageManager
        val userSettingsRepository = UserSettingsRepositoryImpl(
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
        val db = DB.get(appContext, isInMemory = true).apply {
            clearAllTables()
        }
        val appRepository = AppRepositoryImpl(
            db.filterConditionDao(),
            db.targetAppDao(),
            testDispatcher
        )
        addTargetAppUseCase = AddTargetAppUseCaseImpl(appRepository)
        loadAppUseCase = LoadAppUseCaseImpl(userSettingsRepository, appRepository)
        deleteTargetAppUseCase = DeleteTargetAppUseCaseImpl(appRepository)
        packageVisibilityGrantedUseCase =
            PackageVisibilityGrantedUseCaseImpl(userSettingsRepository)
        saveFilterConditionUseCase = SaveFilterConditionUseCaseImpl(appRepository)
        toggleIgnoreSummaryUseCase = ToggleIgnoreSummaryUseCaseImpl(appRepository)
        loadFilterConditionUseCase = LoadFilterConditionUseCaseImpl(appRepository)
        saveAddressUseCase = SaveAddressUseCaseImpl(userSettingsRepository)
        loadAddressUseCase = LoadAddressUseCaseImpl(userSettingsRepository)
        notifyUseCase = NotifyUseCaseImpl(userSettingsRepository, testDispatcher)
        exportDataUseCase =
            ExportDataUseCaseImpl(userSettingsRepository, appRepository, testDispatcher)
        importDataUseCase =
            ImportDataUseCaseImpl(userSettingsRepository, appRepository, testDispatcher)
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
        packageVisibilityGrantedUseCase()
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
            isInstanceOf(PermissionDeniedException::class.java)
        }
    }

    @Test
    fun `通知条件の追加、取得、更新`() {
        runTest(testDispatcher) {
            val cond = "test"
            val updatedCond = "updated"
            val packageName = "com.sample.www"
            val app = InstalledApp("sample", packageName)

            // 追加
            saveFilterConditionUseCase(SaveFilterConditionUseCase.Args(app, cond))
            assertThat(loadFilterConditionUseCase(app)).isEqualTo(
                FilterCondition(
                    packageName,
                    false,
                    cond
                )
            )

            // メッセージ条件の更新
            saveFilterConditionUseCase(SaveFilterConditionUseCase.Args(app, updatedCond))
            assertThat(loadFilterConditionUseCase(app)).isEqualTo(
                FilterCondition(
                    packageName,
                    false,
                    updatedCond
                )
            )

            // サマリー条件の更新
            toggleIgnoreSummaryUseCase.invoke(ToggleIgnoreSummaryUseCase.Args(app))
            assertThat(loadFilterConditionUseCase(app)).isEqualTo(
                FilterCondition(
                    packageName,
                    true,
                    updatedCond
                )
            )
        }
    }

    @Test
    fun `IPアドレスの追加、更新_成功、成功`() {
        val host = "192.168.11.4"
        val port = 5555
        val addr = "$host:$port"
        assertThat(saveAddressUseCase(addr).getOrNull()).isNotNull()
        assertThat(loadAddressUseCase()).isEqualTo(addr)

        val updatedHost = "192.168.11.2"
        val updatedPort = 3456
        val updatedAddr = "$updatedHost:$updatedPort"
        assertThat(saveAddressUseCase(updatedAddr).getOrNull()).isNotNull()
        assertThat(loadAddressUseCase()).isEqualTo(updatedAddr)
    }

    @Test
    fun `IPアドレスの追加、更新_成功、失敗`() {
        val host = "192.168.11.4"
        val port = 5555
        val addr = "$host:$port"
        assertThat(saveAddressUseCase(addr).getOrNull()).isNotNull()
        assertThat(loadAddressUseCase()).isEqualTo(addr)

        val updatedHost = "192.168.11.2"
        val updatedAddr = "$updatedHost:"
        assertThat(saveAddressUseCase(updatedAddr).exceptionOrNull()).isNotNull()
        assertThat(loadAddressUseCase()).isEqualTo(addr)
    }

    @Test
    fun `IPアドレスの追加_失敗_hostなし`() {
        val port = 5555
        val addr = ":$port"
        assertThat(saveAddressUseCase(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portなし`() {
        val host = "192.168.11.4"
        val addr = "$host:"
        assertThat(saveAddressUseCase(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portが数値じゃない`() {
        val host = "192.168.11.4"
        val addr = "$host:test"
        assertThat(saveAddressUseCase(addr).exceptionOrNull()).isNotNull()
    }

    @Test
    fun `通知送信_失敗`() {
        runTest(testDispatcher) {
            assertThat(notifyUseCase("通知テスト").exceptionOrNull()).isNotNull()
        }
    }

    @Test
    @Ignore("FIXME: socket failed: EPERM (Operation not permitted)")
    fun `通知送信_成功`() {
        runTest(testDispatcher) {
            val host = "192.168.11.4"
            val port = 5555
            val addr = "$host:$port"
            saveAddressUseCase(addr)
            assertThat(notifyUseCase("通知テスト").getOrNull()).isNotNull()
        }
    }

    @Test
    fun `バックアップ、復元`() {
        val uri = Uri.fromFile(File.createTempFile(ExportFileName, null, exportFile))
        runTest(testDispatcher) {
            // 初期値の保存
            // ターゲット
            val packageName = "test.export"
            val app = InstalledApp("export", packageName)
            addTargetAppUseCase(app)
            // 条件
            val cond = ".*"
            saveFilterConditionUseCase(SaveFilterConditionUseCase.Args(app, cond))
            toggleIgnoreSummaryUseCase.invoke(ToggleIgnoreSummaryUseCase.Args(app))
            // アドレス
            val addr = "192.168.1.4:5050"
            saveAddressUseCase(addr)

            // バックアップ
            exportDataUseCase(appContext, uri)

            // バックアップ時とは異なるように適当に変更
            // ターゲット
            addTargetAppUseCase(InstalledApp("new", "new"))
            // 条件
            saveFilterConditionUseCase(SaveFilterConditionUseCase.Args(app, "new"))
            toggleIgnoreSummaryUseCase.invoke(ToggleIgnoreSummaryUseCase.Args(app))

            // 復元
            importDataUseCase(appContext, uri)

            // 正常に復元できているか確認
            // ターゲット一覧
            val restoreTargets = loadAppUseCase.loadTargetList()
            assertThat(restoreTargets).apply {
                hasSize(1)
                contains(app)
            }
            // 条件
            val restoreCond = loadFilterConditionUseCase(app)
            assertThat(restoreCond).isEqualTo(FilterCondition(packageName, true, cond))
            // アドレス
            val restoreAddr = loadAddressUseCase()
            assertThat(restoreAddr).isEqualTo(addr)
        }
    }
}