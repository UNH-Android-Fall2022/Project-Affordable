package com.affordable.ui.start.signup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentSignupBinding
import com.affordable.ui.start.splash.SplashFragmentDirections
import com.affordable.utility.isNav


class SingupFragment : BaseFragment<FragmentSignupBinding>() {

    private val TAG = SingupFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {
        with(binding!!) {
            continueSignup.setOnClickListener {
                navController.isNav(R.id.singupFragment) {
                    navController.navigate(SingupFragmentDirections.actionSingupFragmentToPreferencesActivity())
                    activity.finish()
                }
            }
            login.setOnClickListener {
                navController.isNav(R.id.singupFragment) {
                    navController.navigate(SingupFragmentDirections.actionSingupFragmentToLoginFragment())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}