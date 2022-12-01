package com.affordable.ui.start.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentSplashBinding
import com.affordable.ui.start.signup.SingupFragmentDirections
import com.affordable.utility.isNav

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val TAG = SplashFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        if (auth.currentUser != null) {
            showProgress()
            navController.isNav(R.id.splashFragment) {
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToMainActivity())
                activity.finish()
                hideProgress()
            }
        }

    }


    override fun initListener() {

        with(binding!!) {
            signIn.setOnClickListener {
                navController.isNav(R.id.splashFragment) {
                    navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                }
            }
            gettingStarted.setOnClickListener {
                navController.isNav(R.id.splashFragment) {
                    navController.navigate(SplashFragmentDirections.actionSplashFragmentToSingupFragment())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}