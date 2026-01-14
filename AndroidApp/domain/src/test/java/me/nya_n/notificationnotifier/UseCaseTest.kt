package me.nya_n.notificationnotifier

import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.nya_n.notificationnotifier.data.repository.AppRepository
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase
import me.nya_n.notificationnotifier.domain.usecase.SaveAddressUseCase
import me.nya_n.notificationnotifier.domain.usecase.impl.LoadAppUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.NotifyUseCaseImpl
import me.nya_n.notificationnotifier.domain.usecase.impl.SaveAddressUseCaseImpl
import me.nya_n.notificationnotifier.model.AppException.PermissionDeniedException
import me.nya_n.notificationnotifier.model.InstalledApp
import me.nya_n.notificationnotifier.model.UserSettings
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test

@Suppress("NonAsciiCharacters", "RemoveRedundantBackticks")
class UseCaseTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val userSettingsRepository: UserSettingsRepository = mockk()
    private val appRepository: AppRepository = mockk()

    private lateinit var loadAppUseCase: LoadAppUseCaseImpl
    private lateinit var saveAddressUseCase: SaveAddressUseCase
    private lateinit var notifyUseCase: NotifyUseCase

    @Before
    fun setUp() {
        loadAppUseCase = LoadAppUseCaseImpl(userSettingsRepository, appRepository, testDispatcher)
        saveAddressUseCase = SaveAddressUseCaseImpl(userSettingsRepository)
        notifyUseCase = NotifyUseCaseImpl(userSettingsRepository, testDispatcher)

        mockkStatic(android.text.TextUtils::class)
        every { android.text.TextUtils.isDigitsOnly(any()) } answers {
            val str = it.invocation.args[0] as? CharSequence
            str?.all { char -> char.isDigit() } == true
        }
    }

    @Test
    fun `インストール済みアプリの取得_許可あり`() {
        every { userSettingsRepository.getUserSettings() } returns UserSettings("", 0, true)
        every { appRepository.loadInstalledAppList() } returns listOf(InstalledApp("", ""))

        val ret = loadAppUseCase.loadInstalledAppList()
        assertThat(ret.getOrNull()).apply {
            isNotNull()
            isNotEmpty()
        }
    }

    @Test
    fun `インストール済みアプリの取得_許可なし`() {
        every { userSettingsRepository.getUserSettings() } returns UserSettings("", 0, false)
        every { appRepository.loadInstalledAppList() } throws PermissionDeniedException()

        val ret = loadAppUseCase.loadInstalledAppList()
        assertThat(ret.exceptionOrNull()).apply {
            isNotNull()
            isInstanceOf(PermissionDeniedException::class.java)
        }
    }

    @Test
    fun `IPアドレスの追加_成功`() {
        every { userSettingsRepository.getUserSettings() } returns UserSettings(
            "192.168.10.18",
            8484,
            true
        )
        every { userSettingsRepository.saveUserSettings(any()) } just Runs

        assertThat(saveAddressUseCase("192.168.10.18:8484").getOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portなし`() {
        assertThat(saveAddressUseCase("192.168.11.4").exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_hostなし`() {
        assertThat(saveAddressUseCase(":8484").exceptionOrNull()).isNotNull()
    }

    @Test
    fun `IPアドレスの追加_失敗_portが数値じゃない`() {
        assertThat(saveAddressUseCase("192.168.10.18:not_number").exceptionOrNull()).isNotNull()
    }

    @Test
    fun `通知送信_失敗`() {
        runTest(testDispatcher) {
            assertThat(notifyUseCase("通知テスト").exceptionOrNull()).isNotNull()
        }
    }

    @Test
    @LocalOnly
    fun `通知送信_成功`() {
        // CI環境で実行しないようにする
        assumeTrue("Local only", System.getenv("CI") == null)

        // テスト環境のIPアドレスに変更する
        every { userSettingsRepository.getUserSettings() } returns UserSettings(
            "192.168.10.18",
            8484,
            true
        )

        runTest(testDispatcher) {
            assertThat(notifyUseCase("通知テスト").getOrNull()).isNotNull()
        }
    }
}