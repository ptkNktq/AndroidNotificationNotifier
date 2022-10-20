package me.nya_n.notificationnotifier.ui.screen.license

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import me.nya_n.notificationnotifier.model.Fab
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.databinding.FragmentLicenseBinding
import me.nya_n.notificationnotifier.ui.screen.MainViewModel
import me.nya_n.notificationnotifier.ui.util.addEmptyMenuProvider
import me.nya_n.notificationnotifier.ui.util.autoCleared
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LicenseFragment : Fragment() {
    private var binding: FragmentLicenseBinding by autoCleared()
    private val activityModel: MainViewModel by sharedViewModel()

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
        addEmptyMenuProvider()
        binding.web.loadUrl("file:///android_asset/licenses.html")
    }

    override fun onResume() {
        super.onResume()
        activityModel.changeFabState(Fab(false))
    }
}