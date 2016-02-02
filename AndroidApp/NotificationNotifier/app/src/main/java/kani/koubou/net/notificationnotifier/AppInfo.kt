package kani.koubou.net.notificationnotifier

import android.graphics.drawable.Drawable

class AppInfo(val packageName: String, val name: String, val icon: Drawable)
{
    override fun toString(): String
    {
        return packageName + ", " + name
    }
}