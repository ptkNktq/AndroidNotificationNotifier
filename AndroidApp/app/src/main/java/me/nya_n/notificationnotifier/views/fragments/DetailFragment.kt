package me.nya_n.notificationnotifier.views.fragments

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
import me.nya_n.notificationnotifier.utils.Snackbar
import me.nya_n.notificationnotifier.viewmodels.DetailViewModel
import me.nya_n.notificationnotifier.viewmodels.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val model: DetailViewModel by viewModel {
        parametersOf(args.app)
    }
    private val shared: SharedViewModel by sharedViewModel()
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
            it.model = model
            it.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observes()
    }

    private fun initViews() {
        binding.delete.setOnClickListener {
            model.deleteTarget()
        }
    }

    private fun observes() {
        model.message.observe(viewLifecycleOwner) {
            val message = it.getContentIfNotHandled() ?: return@observe
            Snackbar.create(requireView(), message).show()
        }
        model.targetDeleted.observe(viewLifecycleOwner) {
            shared.loadApps()
            findNavController().popBackStack()
        }
    }
}