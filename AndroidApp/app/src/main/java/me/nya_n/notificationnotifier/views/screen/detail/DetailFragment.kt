package me.nya_n.notificationnotifier.views.screen.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.FragmentDetailBinding
import me.nya_n.notificationnotifier.entities.Fab
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.views.screen.MainViewModel
import me.nya_n.notificationnotifier.views.screen.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModel {
        parametersOf(args.app)
    }
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val activityViewModel: MainViewModel by sharedViewModel()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentDetailBinding>(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        ).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
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
        binding.delete.setOnClickListener {
            viewModel.deleteTarget()
        }
    }

    private fun observes() {
        viewModel.message.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            Snackbar.create(requireView(), message).show()
        }
        viewModel.targetDeleted.observe(viewLifecycleOwner) {
            sharedViewModel.loadApps()
            findNavController().popBackStack()
        }
    }
}