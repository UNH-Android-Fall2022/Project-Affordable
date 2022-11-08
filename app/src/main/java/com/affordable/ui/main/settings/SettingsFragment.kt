package com.affordable.ui.main.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentSettingsBinding
import com.affordable.utility.isNav


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val TAG = SettingsFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {
            logout.setOnClickListener {
                navController.isNav(R.id.settingsFragment) {
                    navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToStartActivity())
                    activity.finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}