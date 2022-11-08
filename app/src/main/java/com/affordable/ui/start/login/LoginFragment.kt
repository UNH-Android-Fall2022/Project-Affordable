package com.affordable.ui.start.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentLoginBinding
import com.affordable.ui.start.splash.SplashFragmentDirections
import com.affordable.utility.isNav

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val TAG = LoginFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity



    }


    override fun initListener() {

        with(binding!!){
            signup.setOnClickListener {
                navController.isNav(R.id.loginFragment) {
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToSingupFragment())
                }
            }
            continueLogin.setOnClickListener {
                navController.isNav(R.id.loginFragment) {
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                    activity.finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}