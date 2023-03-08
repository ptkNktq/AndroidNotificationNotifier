package me.nya_n.notificationnotifier.ui.util

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import me.nya_n.notificationnotifier.model.Message

object Snackbar {
    fun create(view: View, message: Message): Snackbar {
        return when (message) {
            is Message.Notice -> notice(view, message)
            is Message.Error -> error(view, message)
        }
    }

    fun notice(view: View, message: Message.Notice): Snackbar {
        val text = view.context.getString(message.message, message.args)
        return Snackbar.make(view, text, Snackbar.LENGTH_LONG)
    }

    fun error(view: View, message: Message.Error): Snackbar {
        return Snackbar.make(view, message.message, Snackbar.LENGTH_INDEFINITE)
            .apply {
                getView().setBackgroundColor(Color.parseColor("#dc143c"))
                setActionTextColor(Color.WHITE)
                setAction("[OK]") {
                    dismiss()
                }
            }
    }
}

@BindingAdapter("image")
fun ImageView.image(bitmap: Bitmap) {
    setImageBitmap(bitmap)
}

/**
 * 現在Preview中か？
 *  - Composableな関数内でのみ使用可能
 */
@Composable
fun isInPreview(): Boolean {
    /*
     * Previewを端末(実機/エミュ問わず)で実行するとLocalInspectionMode.currentがfalseになってしまう
     * そういった状況でも強制的にPreviewモードとして扱いたいときのためにisForcePreviewを用意
     */
    val isForcePreview = false
    return LocalInspectionMode.current || isForcePreview
}
