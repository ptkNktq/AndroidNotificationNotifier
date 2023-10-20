package me.nya_n.notificationnotifier.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

/** 現在Preview中か？
 *   - Composableな関数内でのみ使用可能
 */
@Composable
fun isInPreview(): Boolean {
    // Previewを端末(実機/エミュ問わず)で実行するとLocalInspectionMode.currentがfalseになってしまう
    // そういった状況でも強制的にPreviewモードとして扱いたいときのためにisForcePreviewを用意
    val isForcePreview = false
    return LocalInspectionMode.current || isForcePreview
}
