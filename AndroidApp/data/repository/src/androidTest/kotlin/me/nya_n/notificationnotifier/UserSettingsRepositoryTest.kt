package me.nya_n.notificationnotifier

import androidx.core.content.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.data.repository.impl.UserSettingsRepositoryImpl
import me.nya_n.notificationnotifier.data.repository.source.UserSettingsDataStore
import me.nya_n.notificationnotifier.data.repository.util.SharedPreferenceProvider
import me.nya_n.notificationnotifier.model.UserSettings
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters")
@RunWith(AndroidJUnit4::class)
class UserSettingsRepositoryTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var userSettingsRepository: UserSettingsRepository

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
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
    }

    @Test
    fun `ユーザー設定の保存、取得`() {
        runTest(testDispatcher) {
            val data = UserSettings("192.168.10.18", 8484, false)
            userSettingsRepository.saveUserSettings(data)
            assertThat(userSettingsRepository.getUserSettings()).isEqualTo(data)
        }
    }
}