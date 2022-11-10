package com.affordable.ui.start.signup

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.network.UserModel
import com.affordable.databinding.FragmentSignupBinding
import com.affordable.utility.isNav
import com.affordable.utility.isValidEmail
import com.affordable.utility.isValidPassword
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


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
                if (networkHelper.isNetworkConnected()) {
                    if (checkAndValidate()) {
                        showProgress()
                        auth.createUserWithEmailAndPassword(
                            email.text.toString().trim(),
                            password.text.toString().trim()
                        )
                            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                                checkAnnAddUserToFirestore(task)
                            })
                    }
                }else{
                    showToastShort("No internet connected")
                }
            }
            login.setOnClickListener {
                navController.isNav(R.id.singupFragment) {
                    navController.navigate(SingupFragmentDirections.actionSingupFragmentToLoginFragment())
                }
            }
        }
    }

    private fun checkAnnAddUserToFirestore(task: Task<AuthResult>) {
        hideProgress()
        if (task.isSuccessful) {
            showProgress()
            mFirestore.collection("Users").document().set(
                UserModel(
                    firstName = binding!!.fullname.text.toString(),
                    userId = auth.currentUser?.uid.toString(),
                    userEmail = auth.currentUser?.email.toString(),
                    password = binding!!.password.text.toString().trim()
                )
            ).addOnSuccessListener {
                hideProgress()
                showToastShort("Welcome ${binding!!.fullname.text.toString()}")
                navController.isNav(R.id.singupFragment) {
                    navController.navigate(SingupFragmentDirections.actionSingupFragmentToPreferencesActivity())
                    activity.finish()
                }
            }.addOnFailureListener { e ->
                hideProgress()
                Log.w(TAG, "Error writing document", e)
                showToastShort("Registration Failed")
            }
        } else {
            showToastLong("Registration Failed: " + task.exception?.localizedMessage)
        }
    }

    private fun checkAndValidate(): Boolean {
        with(binding!!) {
            if (fullname.text.toString().trim().isEmpty()) {
                showToastShort("Name is Missing")
                return false
            } else if (!email.text.toString().trim().isValidEmail()) {
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