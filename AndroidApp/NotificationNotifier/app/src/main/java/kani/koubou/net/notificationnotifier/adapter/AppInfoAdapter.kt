package kani.koubou.net.notificationnotifier.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kani.koubou.net.notificationnotifier.AppInfo
import kani.koubou.net.notificationnotifier.R

class AppInfoAdapter(context: Context) : ArrayAdapter<AppInfo>(context, -1)
{
    val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View?
    {
        val view = convertView ?: inflater.inflate(R.layout.adapter_target, null)

        try
        {
            val info = getItem(position)
            (view.findViewById(R.id.titleTextView) as TextView).text = info.name
            (view.findViewById(R.id.iconImageView) as ImageView).setImageDrawable(info.icon)
        }
        catch(e: Exception)
        {
            e.printStackTrace()
        }

        return view
    }
}