package com.affordable.ui.start.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.network.UserModel
import com.affordable.databinding.BottomsheetSignupBinding
import com.affordable.databinding.FragmentLoginBinding
import com.affordable.ui.start.signup.SingupFragmentDirections
import com.affordable.utility.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val TAG = LoginFragment::class.java.name

    lateinit var activity: AppCompatActivity

    private lateinit var googleSignInClient: GoogleSignInClient

    private var verifyOTP: Boolean = false
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = requireActivity() as AppCompatActivity

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)

        setUpPhoneAuthCallbacks()
    }

    private fun setUpPhoneAuthCallbacks() {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                hideProgress()

                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                hideProgress()

                verifyOTP = false

                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {

                    showToastShort("Auth Invalid Credentials")
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    showToastShort("Too Many Requests")
                } else {
                    showToastShort("Phone Auth failed")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                hideProgress()

                Log.d(TAG, "onCodeSent:$verificationId")

                storedVerificationId = verificationId
                resendToken = token

            }
        }
    }

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        checkAlreadyLogin()

    }


    override fun initListener() {

        with(binding!!) {

            addClickEffect(continuePhoneLayout)
            addClickEffect(continueGoogleLayout)

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

            continueGoogleLayout.setOnClickListener {
                signIn()
            }

            continuePhoneLayout.setOnClickListener {
                buttonEffect(it)
                openPhoneAuthDialogue()
            }
        }
    }

    private fun checkAlreadyLogin() {
        if (auth.currentUser != null) {
            showProgress()
            navController.isNav(R.id.singupFragment) {
                navController.navigate(SingupFragmentDirections.actionSingupFragmentToMainActivity())
                activity.finish()
                hideProgress()
            }
        }
    }

    private fun checkForLogin(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            Log.d(TAG, "signInWithEmail:success")
            showToastShort("Sign In Success")
            navController.isNav(R.id.loginFragment) {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                activity.finish()
                hideProgress()
            }
        } else {
            hideProgress()
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

    private fun checkAndAddUserToFirestore(task: Task<AuthResult>, userName: String) {
        if (task.isSuccessful) {
            showProgress()
            mFirestore.collection("Users").document(auth.currentUser?.uid.toString()).set(
                UserModel(
                    username = userName,
                    userId = auth.currentUser?.uid.toString(),
                    userEmail = auth.currentUser?.email.toString(),
                )
            ).addOnSuccessListener {
                showToastShort("Welcome ${userName}")
                navController.isNav(R.id.loginFragment) {
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToPreferencesActivity())
                    activity.finish()
                    hideProgress()
                }
            }.addOnFailureListener { e ->
                hideProgress()
                Log.w(TAG, "Error writing document", e)
                showToastShort("Registration Failed")
            }
        } else {
            hideProgress()
            showToastLong("Registration Failed: " + task.exception?.localizedMessage)
        }
    }

    // GMAIL

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!, account.displayName.toString())
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, username: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    if (task.result?.additionalUserInfo!!.isNewUser) {
                        checkAndAddUserToFirestore(task, username)
                    } else {
                        showToastShort("Sign In Success")
                        navController.isNav(R.id.loginFragment) {
                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                            activity.finish()
                            hideProgress()
                        }
                    }
                } else {
                    hideProgress()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showToastShort(task.exception.toString())
                }
            }
    }

    private fun signIn() {
        showProgress()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    // Phone

    private fun openPhoneAuthDialogue() {

        verifyOTP = false

        val bottomSheetDialog = BottomSheetDialog(activity)
        val mBottomSheetBinding = BottomsheetSignupBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setContentView(mBottomSheetBinding.root)

        mBottomSheetBinding.title.text = "Continue with Phone"
        mBottomSheetBinding.usernameLayout.visibility = View.GONE

        mBottomSheetBinding.sendOtp.setOnClickListener {
            if (verifyOTP) {
                if (storedVerificationId != null) {
                    if (mBottomSheetBinding.otp.text.toString().trim().isEmpty()) {
                        showToastShort("OTP is Empty")
                        return@setOnClickListener
                    }
                    verifyPhoneNumberWithCode(
                        verificationId = storedVerificationId,
                        code = mBottomSheetBinding.otp.text.toString().trim()
                    )
                    showProgress()
                    bottomSheetDialog.dismiss()
                }
            } else {
                if (mBottomSheetBinding.edtMobile.text.toString().trim().isEmpty()) {
                    showToastShort("Phone Number is Empty")
                } else {

                    verifyOTP = true

                    mBottomSheetBinding.otpLayout.visibility = View.VISIBLE
                    mBottomSheetBinding.sendOtp.text = "Verify OTP"
                    val phoneNumber =
                        mBottomSheetBinding.ccp.selectedCountryCodeWithPlus + mBottomSheetBinding.edtMobile.text.toString()
                            .trim()
                    startPhoneNumberVerification(phoneNumber)
                    showProgress()
                }
            }
        }

        bottomSheetDialog.show()
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {

                    Log.d(TAG, "signInWithCredential:success")

                    if (task.result?.additionalUserInfo!!.isNewUser) {
                        showToastShort("Please Sign Up First then Login")
                        auth.currentUser?.delete() // because it comes unnecessary
                        hideProgress()
                    } else {
                        showToastShort("Sign In Success")
                        navController.isNav(R.id.loginFragment) {
                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                            activity.finish()
                            hideProgress()
                        }
                    }
                } else {
                    hideProgress()
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }
}