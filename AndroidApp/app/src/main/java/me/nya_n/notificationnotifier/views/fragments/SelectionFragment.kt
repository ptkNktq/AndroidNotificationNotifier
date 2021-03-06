package me.nya_n.notificationnotifier.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_selection.*
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.FragmentSelectionBinding
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.viewmodels.SelectionViewModel
import me.nya_n.notificationnotifier.viewmodels.SharedViewModel
import me.nya_n.notificationnotifier.views.adapters.AppAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SelectionFragment : Fragment() {

    private val model: SelectionViewModel by viewModel()
    private val shared: SharedViewModel by sharedViewModel()
    private val filter = object: AppAdapter.Filter {
        var query = ""

        override fun filter(items: List<InstalledApp>): List<InstalledApp> {
            return if (query.isEmpty()) {
                items
            } else {
                val q = query.toLowerCase(Locale.getDefault())
                items.filter {
                    it.lowerLabel.contains(q)
                }
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragmentSelectionBinding>(
            inflater,
            R.layout.fragment_selection,
            container,
            false
        ).also{
            it.lifecycleOwner = this
            it.model = model
            it.shared = shared
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observes()
    }

    private fun initViews() {
        val adapter = AppAdapter(filter) { app ->
            shared.addTarget(app)
        }
        list.apply {
            val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    private fun observes() {
        model.query.observe(viewLifecycleOwner) {
            filter.query = it
            val adapter = list.adapter as AppAdapter
            adapter.targetChanged()
        }
        shared.list.observe(viewLifecycleOwner) {
            val adapter = list.adapter as AppAdapter
            adapter.addAll(it)
        }
        shared.addedMessage.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            Snackbar.create(requireView(), message).show()
        }
    }
}