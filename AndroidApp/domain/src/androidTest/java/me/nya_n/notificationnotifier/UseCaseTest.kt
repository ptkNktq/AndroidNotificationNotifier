package me.nya_n.notificationnotifier

import android.net.Uri
import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.nya_n.notificationnotifier.data.repository.impl.AppRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.impl.BackupRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.impl.UserSettingsRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.data.repository.source.UserSettingsDataStore
import me.nya_n.notificationnotifier.data.repository.util.SharedPreferenceProvider
import me.nya_n.notificationnotifier.domain.usecase.AddTargetAppUseCase
import me.nya_n.notificationnotifier.domain.usecase.ExportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.ImportDataUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.LoadFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveFilterConditionUseCase
import me.nya_n.notificationnotifier.domain.usecase.ToggleIgnoreSummaryUseCase
import me.nya_n.notificationnotifier.domain.usecase.impl.AddTargetAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ExportDataUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ImportDataUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadAddressUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadFilterConditionUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.SaveAddressUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.SaveFilterConditionUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.ToggleIgnoreSummaryUseCaseImpl
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@Suppress("NonAsciiCharacters")
@RunWith(AndroidJUnit4::class)
class UseCaseTest {
    companion object {
        private const val ExportFileName: String = "export.json"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var exportFile: File

    private lateinit var addTargetAppUseCase: AddTargetAppUseCase
    private lateinit var loadAppUseCase: LoadAppUseCaseImpl
    private lateinit var saveFilterConditionUseCase: SaveFilterConditionUseCase
    private lateinit var toggleIgnoreSummaryUseCase: ToggleIgnoreSummaryUseCase
    private lateinit var loadFilterConditionUseCase: LoadFilterConditionUseCase
    private lateinit var saveAddressUseCase: SaveAddressUseCase
    private lateinit var loadAddressUseCase: LoadAddressUseCase
    private lateinit var exportDataUseCase: ExportDataUseCase
    private lateinit var importDataUseCase: ImportDataUseCase

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        exportFile = appContext.filesDir
        File(exportFile, ExportFileName).apply {
            if (exists()) {
                delete()
            }
        }
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
            appContext.packageManager,
            db.filterConditionDao(),
            db.targetAppDao(),
            testDispatcher
        )
        val backupRepository = BackupRepositoryImpl(appContext, testDispatcher)
        addTargetAppUseCase = AddTargetAppUseCaseImpl(appRepository)
        loadAppUseCase = LoadAppUseCaseImpl(userSettingsRepository, appRepository, testDispatcher)
        saveFilterConditionUseCase = SaveFilterConditionUseCaseImpl(appRepository)
        toggleIgnoreSummaryUseCase = ToggleIgnoreSummaryUseCaseImpl(appRepository)
        loadFilterConditionUseCase = LoadFilterConditionUseCaseImpl(appRepository)
        saveAddressUseCase = SaveAddressUseCaseImpl(userSettingsRepository)
        loadAddressUseCase = LoadAddressUseCaseImpl(userSettingsRepository)
        exportDataUseCase =
            ExportDataUseCaseImpl(userSettingsRepository, appRepository, backupRepository)
        importDataUseCase =
            ImportDataUseCaseImpl(userSettingsRepository, appRepository, backupRepository)
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
            exportDataUseCase(uri)

            // バックアップ時とは異なるように適当に変更
            // ターゲット
            addTargetAppUseCase(InstalledApp("new", "new"))
            // 条件
            saveFilterConditionUseCase(SaveFilterConditionUseCase.Args(app, "new"))
            toggleIgnoreSummaryUseCase.invoke(ToggleIgnoreSummaryUseCase.Args(app))

            // 復元
            importDataUseCase(uri)

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