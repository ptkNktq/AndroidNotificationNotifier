package me.nya_n.notificationnotifier.views.screen.license

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import me.nya_n.notificationnotifier.R
import me.nya_n.notificationnotifier.databinding.FragmentLicenseBinding
import me.nya_n.notificationnotifier.entities.Fab
import me.nya_n.notificationnotifier.views.screen.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LicenseFragment : Fragment() {
    private lateinit var binding: FragmentLicenseBinding
    private val activityModel: MainViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_license,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.web.loadUrl("file:///android_asset/licenses.html")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onResume() {
        super.onResume()
        activityModel.changeFabState(Fab(false))
    }
}