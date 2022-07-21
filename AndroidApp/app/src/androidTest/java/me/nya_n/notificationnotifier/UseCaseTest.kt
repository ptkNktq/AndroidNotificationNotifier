package me.nya_n.notificationnotifier

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import me.nya_n.notificationnotifier.repositories.UserSettingRepository
import org.junit.Before
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UseCaseTest {

    private lateinit var userSettingRepository: UserSettingRepository

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        userSettingRepository = UserSettingRepository(appContext)
    }
}