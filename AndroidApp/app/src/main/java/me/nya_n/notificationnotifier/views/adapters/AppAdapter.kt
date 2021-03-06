package me.nya_n.notificationnotifier.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.entities.InstalledApp

class AppAdapter(
    private val filter: Filter,
    private val callback: ((InstalledApp) -> Unit)? = null
) : RecyclerView.Adapter<AppAdapter.VH>() {

    private val original = ArrayList<InstalledApp>()
    private val items = ArrayList<InstalledApp>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.component_app,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = items[position]
        holder.itemView.setOnClickListener {
            callback?.invoke(data)
        }
        holder.name.text = data.label
        holder.icon.setImageDrawable(data.icon)
    }

    fun addAll(elements: List<InstalledApp>) {
        original.addAll(elements)
        targetChanged()
    }

    fun targetChanged() {
        val filtered = filter.filter(original)
        items.clear()
        items.addAll(filtered)
        notifyDataSetChanged()
    }

    class VH(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val icon: ImageView by lazy { view.findViewById(R.id.icon) }
        val name: TextView by lazy { view.findViewById(R.id.name) }
    }

    interface Filter {
        fun filter(items: List<InstalledApp>): List<InstalledApp>
    }
}