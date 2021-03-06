package me.nya_n.notificationnotifier.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.FragmentMainBinding
import me.nya_n.notificationnotifier.entities.InstalledApp
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.viewmodels.MainViewModel
import me.nya_n.notificationnotifier.viewmodels.SharedViewModel
import me.nya_n.notificationnotifier.views.adapters.AppAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val model: MainViewModel by viewModel()
    private val shared: SharedViewModel by sharedViewModel()
    private val filter = object : AppAdapter.Filter {
        private val targets = ArrayList<String>()

        override fun filter(items: List<InstalledApp>): List<InstalledApp> {
            return items.filter { app ->
                targets.contains(app.packageName)
            }
        }

        fun targetChanged(elements: List<String>) {
            targets.clear()
            targets.addAll(elements)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        ).also {
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
            shared.deleteTarget(app)
        }
        list.apply {
            val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun observes() {
        model.message.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            Snackbar.create(requireView(), message).show()
        }
        shared.targets.observe(viewLifecycleOwner) {
            filter.targetChanged(it)
            val adapter = list.adapter as AppAdapter
            adapter.targetChanged()
        }
        shared.list.observe(viewLifecycleOwner) {
            val adapter = list.adapter as AppAdapter
            adapter.addAll(it)
        }
        shared.deletedMessage.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            Snackbar.create(requireView(), message).show()
        }
    }
}