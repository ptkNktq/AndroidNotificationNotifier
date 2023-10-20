package me.nya_n.notificationnotifier.domain.usecase

/**
 * ユーザー設定からIPアドレスを取得する
 */
interface LoadAddressUseCase {
    operator fun invoke(): String
}