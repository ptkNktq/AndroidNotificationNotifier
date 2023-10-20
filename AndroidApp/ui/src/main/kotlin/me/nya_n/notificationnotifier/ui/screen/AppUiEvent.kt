package me.nya_n.notificationnotifier.ui.screen

// UiEvent消化時にオブジェクトを比較したいのでdata classやobjectではなくclassを使う
// classじゃないと同じイベントを消してしまうことがある
abstract class AppUiEvent {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}