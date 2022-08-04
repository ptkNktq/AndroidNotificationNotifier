package me.nya_n.notificationnotifier.views.screen.top

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.FragmentTopBinding
import me.nya_n.notificationnotifier.domain.entities.Backup
import me.nya_n.notificationnotifier.domain.entities.Fab
import me.nya_n.notificationnotifier.domain.entities.InstalledApp
import me.nya_n.notificationnotifier.domain.entities.Message
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.utils.addMenuProvider
import me.nya_n.notificationnotifier.utils.autoCleared
import me.nya_n.notificationnotifier.views.adapters.AppAdapter
import me.nya_n.notificationnotifier.views.dialogs.PackageVisibilityDialog
import me.nya_n.notificationnotifier.views.screen.MainViewModel
import me.nya_n.notificationnotifier.views.screen.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopFragment : Fragment() {
    private var binding: FragmentTopBinding by autoCleared()
    private val viewModel: TopViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val activityViewModel: MainViewModel by sharedViewModel()
    private val filter = object : AppAdapter.Filter {
        private val targets = ArrayList<InstalledApp>()

        override fun filter(items: List<InstalledApp>): List<InstalledApp> {
            return items.filter { app ->
                targets.any { t -> t.packageName == app.packageName }
            }
        }

        fun targetChanged(elements: List<InstalledApp>) {
            targets.clear()
            targets.addAll(elements)
        }
    }

    private val exportDataResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_CANCELED) return@registerForActivityResult
            val uri = it.data?.data
            if (uri != null) {
                viewModel.exportData(requireContext(), uri)
            } else {
                handleMessage(Message.Error(R.string.export_failed))
            }
        }

    private val importDataResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_CANCELED) return@registerForActivityResult
            val uri = it.data?.data
            if (uri != null) {
                viewModel.importData(requireContext(), uri)
            } else {
                handleMessage(Message.Error(R.string.import_failed))
            }
        }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu, menu)
        }

        override fun onMenuItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.export_data -> {
                    exportDataResult.launch(
                        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/json"
                            putExtra(Intent.EXTRA_TITLE, Backup.FILE_NAME)
                        }
                    )
                    true
                }
                R.id.import_data -> {
                    importDataResult.launch(
                        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/json"
                        }
                    )
                    true
                }
                R.id.license -> {
                    findNavController().navigate(R.id.action_MainFragment_to_LicenseFragment)
                    true
                }
                else -> false
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentTopBinding>(
            inflater,
            R.layout.fragment_top,
            container,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it.sharedViewModel = sharedViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addMenuProvider(menuProvider)
        initViews()
        observes()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.changeFabState((Fab(true) {
            findNavController().navigate(R.id.action_MainFragment_to_SelectionFragment)
        }))
    }

    private fun initViews() {
        val adapter = AppAdapter(requireContext().packageManager, filter) { app ->
            findNavController().navigate(
                TopFragmentDirections.actionMainFragmentToDetailFragment(app)
            )
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
        viewModel.message.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            handleMessage(message)
        }
        sharedViewModel.targets.observe(viewLifecycleOwner) {
            filter.targetChanged(it)
            val adapter = binding.list.adapter as AppAdapter
            adapter.targetChanged()
        }
        sharedViewModel.list.observe(viewLifecycleOwner) {
            binding.refresh.isRefreshing = false
            (binding.list.adapter as AppAdapter).apply {
                clear()
                addAll(it)
            }
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

    private fun handleMessage(message: Message) {
        Snackbar.create(binding.root, message).show()
    }
}