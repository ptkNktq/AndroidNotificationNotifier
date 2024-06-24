package me.nya_n.notificationnotifier.domain.usecase

/** IPアドレスを保存する */
interface SaveAddressUseCase {
    operator fun invoke(address: String?): Result<Unit>
}