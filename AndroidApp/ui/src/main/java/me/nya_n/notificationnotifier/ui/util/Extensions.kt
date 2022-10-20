package me.nya_n.notificationnotifier.ui.util

import android.graphics.Bitmap
import android.graphics.Color
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.view.MenuProvider
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
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

fun Fragment.addMenuProvider(provider: MenuProvider) {
    requireActivity().addMenuProvider(provider)
}

fun Fragment.addEmptyMenuProvider() {
    addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    })
}

@BindingAdapter("visible")
fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("image")
fun ImageView.image(bitmap: Bitmap) {
    setImageBitmap(bitmap)
}