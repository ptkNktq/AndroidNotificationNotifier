package me.nya_n.notificationnotifier.domain.usecase.dummy

import me.nya_n.notificationnotifier.domain.usecase.CheckPackageVisibilityUseCase

/** 最初2回の実行ではfalse、それ以降の実行ではtrueを返すダミー実装 */
class CheckPackageVisibilityUseCaseDummyImpl : CheckPackageVisibilityUseCase {
    private var count = 0

    override fun invoke(): Boolean {
        return count++ > 1
    }
}