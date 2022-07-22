package me.nya_n.notificationnotifier.views.screen.selection

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
import me.nya_n.notificationnotifier.domain.entities.Fab
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.views.adapters.AppAdapter
import me.nya_n.notificationnotifier.views.dialogs.PackageVisibilityDialog
import me.nya_n.notificationnotifier.views.screen.MainViewModel
import me.nya_n.notificationnotifier.views.screen.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectionFragment : Fragment() {
    private lateinit var binding: FragmentSelectionBinding
    private val viewModel: SelectionViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val activityViewModel: MainViewModel by sharedViewModel()
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
            it.viewModel = viewModel
            it.sharedViewModel = sharedViewModel
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
        activityViewModel.changeFabState(Fab(false))
    }

    private fun initViews() {
        val adapter = AppAdapter(requireContext().packageManager, filter) { app ->
            viewModel.addTarget(app)
        }
        binding.list.apply {
            val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        binding.refresh.setOnRefreshListener {
            sharedViewModel.loadApps()
        }
    }

    private fun observes() {
        viewModel.query.observe(viewLifecycleOwner) {
            filter.query = it
            val adapter = binding.list.adapter as AppAdapter
            adapter.targetChanged()
        }
        viewModel.message.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            Snackbar.create(requireView(), message).show()
        }
        viewModel.targetAdded.observe(viewLifecycleOwner) {
            sharedViewModel.loadApps()
        }
        sharedViewModel.list.observe(viewLifecycleOwner) {
            binding.refresh.isRefreshing = false
            (binding.list.adapter as AppAdapter).apply {
                clear()
                addAll(it)
            }
        }
        sharedViewModel.targets.observe(viewLifecycleOwner) {
            filter.targetChanged(it)
            val adapter = binding.list.adapter as AppAdapter
            adapter.targetChanged()
        }
        sharedViewModel.checkPackageVisibilityEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled() ?: return@observe
            binding.refresh.isRefreshing = false
            PackageVisibilityDialog.showOnlyOnce(childFragmentManager)
            childFragmentManager.setFragmentResultListener(
                PackageVisibilityDialog.TAG,
                viewLifecycleOwner
            ) { _, result ->
                val isGranted = result.getBoolean(PackageVisibilityDialog.KEY_IS_GRANTED)
                if (isGranted) {
                    sharedViewModel.packageVisibilityGranted()
                    sharedViewModel.loadApps()
                    binding.refresh.isRefreshing = true
                }
            }
        }
    }
}