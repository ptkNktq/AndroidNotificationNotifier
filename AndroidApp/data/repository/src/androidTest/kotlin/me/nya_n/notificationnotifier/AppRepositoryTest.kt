package me.nya_n.notificationnotifier

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.impl.AppRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.source.DB
import me.nya_n.notificationnotifier.model.FilterCondition
import me.nya_n.notificationnotifier.model.InstalledApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters")
@RunWith(AndroidJUnit4::class)
class AppRepositoryTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var appRepository: AppRepository

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = DB.get(appContext, isInMemory = true).apply {
            clearAllTables()
        }
        appRepository = AppRepositoryImpl(
            appContext.packageManager,
            db.filterConditionDao(),
            db.targetAppDao(),
            testDispatcher
        )
    }

    @Test
    fun `通知対象アプリの追加、取得、削除`() {
        runTest(testDispatcher) {
            val app = InstalledApp("sample", "com.sample.www")
            appRepository.addTargetApp(app)

            val added = appRepository.getTargetAppList()
            assertThat(added).hasSize(1)
            assertThat(added.first()).isEqualTo(app)

            appRepository.deleteTargetApp(app)
            val deleted = appRepository.getTargetAppList()
            assertThat(deleted).isEmpty()
        }
    }


    @Test
    fun `通知条件の追加、取得、更新`() {
        runTest(testDispatcher) {
            val packageName = "com.sample.www"

            // データなし
            assertThat(appRepository.getFilterCondition(packageName)).isNull()
            assertThat(appRepository.getFilterConditionOrDefault(packageName))
                .isEqualTo(FilterCondition.default(packageName))

            // 追加
            val added = FilterCondition(packageName, false, "test")
            appRepository.saveFilterCondition(added)
            assertThat(appRepository.getFilterCondition(packageName)).isEqualTo(added)

            // メッセージ条件の更新
            val updatedCondition = added.copy(condition = "updated")
            appRepository.saveFilterCondition(updatedCondition)
            assertThat(appRepository.getFilterCondition(packageName)).isEqualTo(updatedCondition)

            // サマリー条件の更新
            val updatedSummary = updatedCondition.copy(isIgnoreSummary = true)
            appRepository.saveFilterCondition(updatedSummary)
            assertThat(appRepository.getFilterCondition(packageName)).isEqualTo(updatedSummary)
        }
    }
}