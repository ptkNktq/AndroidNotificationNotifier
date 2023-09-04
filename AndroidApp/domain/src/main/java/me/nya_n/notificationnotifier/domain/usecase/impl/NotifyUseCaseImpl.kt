package me.nya_n.notificationnotifier.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.data.repository.UserSettingsRepository
import me.nya_n.notificationnotifier.domain.usecase.NotifyUseCase
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class NotifyUseCaseImpl(
    private val userSettingsRepository: UserSettingsRepository
) : NotifyUseCase {
    companion object {
        private const val CONNECTION_TIMEOUT = 1_000
    }

    override suspend operator fun invoke(message: String): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val settings = userSettingsRepository.getUserSettings()
                val addr = InetSocketAddress(
                    InetAddress.getByName(settings.host),
                    settings.port
                )
                Socket().use { socket ->
                    socket.connect(addr, CONNECTION_TIMEOUT)
                    socket.getOutputStream().use { writer ->
                        writer.write(message.toByteArray())
                    }
                }
            }
        }
    }
}