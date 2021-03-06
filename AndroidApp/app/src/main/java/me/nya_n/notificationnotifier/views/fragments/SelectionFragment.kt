package me.nya_n.notificationnotifier.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.FragmentSelectionBinding
import me.nya_n.notificationnotifier.entities.Fab
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.utils.Event
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.viewmodels.MainViewModel
import me.nya_n.notificationnotifier.viewmodels.SelectionViewModel
import me.nya_n.notificationnotifier.viewmodels.SharedViewModel
import me.nya_n.notificationnotifier.views.adapters.AppAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SelectionFragment : Fragment() {
    private lateinit var binding: FragmentSelectionBinding
    private val model: SelectionViewModel by viewModel()
    private val shared: SharedViewModel by sharedViewModel()
    private val activityModel: MainViewModel by sharedViewModel()
    private val filter = object : AppAdapter.Filter {
        private val targets = ArrayList<InstalledApp>()
        var query = ""

        override fun filter(items: List<InstalledApp>): List<InstalledApp> {
            val q = query.lowercase()
            return items.filter {
                it.label.lowercase()
                    .contains(q) && !targets.any { t -> t.packageName == it.packageName }
            }
        }

        fun targetChanged(elements: List<InstalledApp>) {
            targets.clear()
            targets.addAll(elements)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentSelectionBinding>(
            inflater,
            R.layout.fragment_selection,
            container,
            false
        ).also {
            it.lifecycleOwner = this
            it.model = model
            it.shared = shared
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observes()
    }

    override fun onResume() {
        super.onResume()
        activityModel.fab.postValue(Event(Fab(false)))
    }

    private fun initViews() {
        val adapter = AppAdapter(requireContext().packageManager, filter) { app ->
            model.addTarget(app)
        }
        binding.list.apply {
            val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    private fun observes() {
        model.query.observe(viewLifecycleOwner) {
            filter.query = it
            val adapter = binding.list.adapter as AppAdapter
            adapter.targetChanged()
        }
        model.message.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            Snackbar.create(requireView(), message).show()
        }
        model.targetAdded.observe(viewLifecycleOwner) {
            shared.loadApps()
        }
        shared.list.observe(viewLifecycleOwner) {
            (binding.list.adapter as AppAdapter).apply {
                clear()
                addAll(it)
            }
        }
        shared.targets.observe(viewLifecycleOwner) {
            filter.targetChanged(it)
            val adapter = binding.list.adapter as AppAdapter
            adapter.targetChanged()
        }
    }
}