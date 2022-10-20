package me.nya_n.notificationnotifier.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nya_n.notificationnotifier.repository.UserSettingRepository
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class NotifyTestUseCase(
    private val userSettingRepository: UserSettingRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val setting = userSettingRepository.getUserSetting()
                val buff = "通知テスト".toByteArray()
                val addr = InetAddress.getByName(setting.host)
                val packet = DatagramPacket(buff, buff.size, addr, setting.port)
                DatagramSocket().apply {
                    send(packet)
                    close()
                }
            }
        }
    }
}