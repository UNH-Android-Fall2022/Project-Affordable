package com.affordable.ui.start.login

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentLoginBinding
import com.affordable.utility.isNav
import com.affordable.utility.isValidEmail
import com.affordable.utility.isValidPassword
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


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

        with(binding!!) {
            signup.setOnClickListener {
                navController.isNav(R.id.loginFragment) {
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToSingupFragment())
                }
            }
            continueLogin.setOnClickListener {
                if (networkHelper.isNetworkConnected()) {
                    if (checkAndValidate()) {
                        showProgress()
                        auth.signInWithEmailAndPassword(
                            email.text.toString().trim(),
                            password.text.toString().trim()
                        )
                            .addOnCompleteListener(requireActivity()) { task ->
                                checkForLogin(task)
                            }
                    }
                } else {
                    showToastShort("No internet connected")
                }
            }
        }
    }

    private fun checkForLogin(task: Task<AuthResult>) {
        hideProgress()
        if (task.isSuccessful) {
            Log.d(TAG, "signInWithEmail:success")
            showToastShort("Sign In Success")
            navController.isNav(R.id.loginFragment) {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                activity.finish()
            }
        } else {
            try {
                throw task.exception!!
            } catch (e: FirebaseAuthInvalidUserException) {
                showToastLong("This User Not Found , Create A New Account")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                showToastLong("The Password Is Invalid, Please Try Valid Password")
            } catch (e: FirebaseNetworkException) {
                showToastLong("Please Check Your Connection")
            } catch (e: Exception) {
                Log.w(TAG, "signInWithEmail:failed", task.exception)
                showToastLong(task.exception?.localizedMessage.toString())
            }
        }
    }

    private fun checkAndValidate(): Boolean {
        with(binding!!) {
            if (!email.text.toString().trim().isValidEmail()) {
                showToastShort("Email is not Valid")
                return false
            } else if (!password.text.toString().trim().isValidPassword()) {
                showToastShort("Password must be 6 digits long")
                return false
            } else {
                return true
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}