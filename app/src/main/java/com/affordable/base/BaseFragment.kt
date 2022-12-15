package com.affordable.base

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.affordable.R
import com.affordable.data.network.*
import com.affordable.ui.main.home.GpsTracker
import com.affordable.utility.NetworkHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseFragment<VB> : Fragment() {

    private val TAG = BaseFragment::class.java.name

    private var mDialog: Dialog? = null
    private var mBinding: VB? = null
    protected val binding: VB? get() = mBinding

    lateinit var mNavController: NavController
    protected open val navController: NavController get() = mNavController

    protected lateinit var mFirestore: FirebaseFirestore
    protected lateinit var auth: FirebaseAuth

    protected lateinit var networkHelper: NetworkHelper

    protected var userLattitude: String = ""
    protected var userLongitude: String = ""

    protected var userModel: UserModel? = null
    protected var shoppingPreferenceModelList: ArrayList<ShoppingPreferenceModel> = ArrayList()
    protected var storesPreferenceModelList: ArrayList<StoresPreferenceModel> = ArrayList()
    protected var cardsPreferenceModelList: ArrayList<UserCardPreferenceModel> = ArrayList()
    protected var dealsList: ArrayList<DealsModel> = ArrayList()

    val requestCodeLocationPermission: Int = 101

    protected var gpsTracker: GpsTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        networkHelper = NetworkHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = initBindingRef(inflater, container)
        return initRoot()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = findNavController()
        init()
        initListener()
    }


    protected abstract fun initBindingRef(inflater: LayoutInflater, container: ViewGroup?): VB
    protected abstract fun initRoot(): View?
    protected abstract fun init()
    protected abstract fun initListener()

    fun showProgress() {
        // no tile for the dialog
        if (mDialog != null && mDialog!!.isShowing()) {
            mDialog!!.dismiss()
            mDialog = null
        }
        mDialog = Dialog(requireContext())
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.progress_bar_dialog)
        mDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mProgressBar: ProgressBar = mDialog!!.findViewById(R.id.progress_bar)
        mProgressBar.visibility = View.VISIBLE
        mProgressBar.isIndeterminate = true
        mDialog!!.setCancelable(false)
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
    }

    fun showToastShort(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    open fun hideProgress() {
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    open fun hideSoftKeyboard(view: View) {
        val inputMethodManager: InputMethodManager = requireActivity().getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText()) {
            if (requireActivity().currentFocus != null) inputMethodManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken, 0
            )
        }
        if (view.hasFocus()) view.clearFocus()
    }

    open fun copyToClipboard(text: String) {
        var clipboard: ClipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("MY_TEXT", text)
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip)
    }

    open fun pasteFromClipboard(): String? {
        var clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return clipboard.primaryClip?.getItemAt(0)?.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    /// Firebase Functions

    fun fetchAllStoresCategoriesCards(callback: () -> Unit) {
        getAllDeals {
            getAllStoresPreferences {
                getAllShoppingPreferences {
                    getAllCardsPreferences {
                        callback()
                    }
                }
            }
        }
    }

    fun getUserProfile(callback: (UserModel) -> Unit) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("Users").document(userId).get().addOnSuccessListener {
                userModel = it.toObject<UserModel>()!!
                callback(userModel!!)
                hideProgress()
            }.addOnFailureListener {
                hideProgress()
            }.addOnCanceledListener {
                hideProgress()
            }
        }
    }

    fun updateUserProfile(
        dob: String, firstname: String, lastname: String, callback: (String) -> Unit
    ) {
        showProgress()
        val newData = hashMapOf(
            "firstName" to firstname,
            "lastName" to lastname,
            "dob" to dob,
        )
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("Users").document(userId).update(newData as Map<String, Any>)
                .addOnSuccessListener {
                    hideProgress()
                    callback("Profile Updated")
                }.addOnFailureListener {
                    hideProgress()
                    callback("Profile Update Failed")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Profile Update Cancelled")
                }
        }
    }

    fun getPrivacyPolicy(
        callback: (PrivacyPolicyModel) -> Unit
    ) {
        showProgress()
        mFirestore.collection("PrivacyPolicy").get().addOnSuccessListener {
            callback(it.documents[0].toObject<PrivacyPolicyModel>()!!)
            hideProgress()
        }.addOnFailureListener {
            hideProgress()
        }.addOnCanceledListener {
            hideProgress()
        }
    }

    fun submitFeedback(rating: String, message: String, callback: (String) -> Unit) {
        showProgress()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDate: String = df.format(Date().time)
        auth.currentUser?.uid?.let { userId ->
            val feedback = FeedbackModel(
                rating,
                message,
                formattedDate,
                userId
            )
            mFirestore
                .collection("Feedback")
                .document()
                .set(feedback)
                .addOnSuccessListener {
                    hideProgress()
                    callback("Feedback Shared")
                }.addOnFailureListener {
                    hideProgress()
                    callback("Feedback Failed")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Feedback Cancelled")
                }
        }
    }

    fun getUserShoppingPreferences(callback: (ArrayList<ShoppingPreferenceModel>) -> Unit) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("ShoppingPreferences")
                .get()
                .addOnSuccessListener {
                    val list: ArrayList<ShoppingPreferenceModel> = ArrayList()
                    for (document in it.documents) {
                        document.toObject<ShoppingPreferenceModel>()
                            ?.let { it1 -> list.add(it1) }
                    }
                    callback(list)
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                }.addOnCanceledListener {
                    hideProgress()
                }
        }
    }

    fun addUserShoppingPreferences(
        shoppingPreferenceModel: ShoppingPreferenceModel,
        callback: (String) -> Unit
    ) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("ShoppingPreferences")
                .document(shoppingPreferenceModel.id)
                .set(shoppingPreferenceModel)
                .addOnSuccessListener {
                    callback("Saved")
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                    callback("Error")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Cancel")
                }
        }
    }

    fun getAllShoppingPreferences(callback: (ArrayList<ShoppingPreferenceModel>) -> Unit) {
        showProgress()
        mFirestore.collection("ShoppingCategories")
            .get()
            .addOnSuccessListener {
                val list: ArrayList<ShoppingPreferenceModel> = ArrayList()
                for (document in it.documents) {
                    document.toObject<ShoppingPreferenceModel>()
                        ?.let { it1 -> list.add(it1) }
                }
                shoppingPreferenceModelList = list
                callback(list)
                hideProgress()
            }.addOnFailureListener {
                hideProgress()
            }.addOnCanceledListener {
                hideProgress()
            }
    }

    fun deleteUserShoppingPreference(
        shoppingPreferenceModel: ShoppingPreferenceModel,
        callback: (String) -> Unit
    ) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("ShoppingPreferences")
                .document(shoppingPreferenceModel.id)
                .delete()
                .addOnSuccessListener {
                    callback("Deleted")
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                    callback("Error")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Cancel")
                }
        }
    }

    fun getUserStoresPreferences(callback: (ArrayList<StoresPreferenceModel>) -> Unit) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("StoresPreferences")
                .get()
                .addOnSuccessListener {
                    val list: ArrayList<StoresPreferenceModel> = ArrayList()
                    for (document in it.documents) {
                        document.toObject<StoresPreferenceModel>()
                            ?.let { it1 -> list.add(it1) }
                    }
                    callback(list)
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                }.addOnCanceledListener {
                    hideProgress()
                }
        }
    }

    fun getAllStoresPreferences(callback: (ArrayList<StoresPreferenceModel>) -> Unit) {
        showProgress()
        mFirestore.collection("Stores")
            .get()
            .addOnSuccessListener {
                val list: ArrayList<StoresPreferenceModel> = ArrayList()
                for (document in it.documents) {
                    document.toObject<StoresPreferenceModel>()
                        ?.let { it1 -> list.add(it1) }
                }
                storesPreferenceModelList = list
                callback(list)
                hideProgress()
            }.addOnFailureListener {
                hideProgress()
            }.addOnCanceledListener {
                hideProgress()
            }
    }

    fun addUserStorePreferences(
        storePreferenceModel: StoresPreferenceModel,
        callback: (String) -> Unit
    ) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("StoresPreferences")
                .document(storePreferenceModel.id)
                .set(storePreferenceModel)
                .addOnSuccessListener {
                    callback("Saved")
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                    callback("Error")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Cancel")
                }
        }
    }

    fun deleteUserStorePreference(
        storePreferenceModel: StoresPreferenceModel,
        callback: (String) -> Unit
    ) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("StoresPreferences")
                .document(storePreferenceModel.id)
                .delete()
                .addOnSuccessListener {
                    callback("Deleted")
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                    callback("Error")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Cancel")
                }
        }
    }

    fun getUserCardsPreferences(callback: (ArrayList<UserCardPreferenceModel>) -> Unit) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("CardsPreferences")
                .get()
                .addOnSuccessListener {
                    val list: ArrayList<UserCardPreferenceModel> = ArrayList()
                    for (document in it.documents) {
                        document.toObject<UserCardPreferenceModel>()
                            ?.let { it1 -> list.add(it1) }
                    }
                    callback(list)
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                }.addOnCanceledListener {
                    hideProgress()
                }
        }
    }

    fun getAllCardsPreferences(callback: (ArrayList<UserCardPreferenceModel>) -> Unit) {
        showProgress()
        mFirestore.collection("Card Type")
            .get()
            .addOnSuccessListener {
                val list: ArrayList<UserCardPreferenceModel> = ArrayList()
                for (document in it.documents) {
                    document.toObject<UserCardPreferenceModel>()
                        ?.let { it1 -> list.add(it1) }
                }
                cardsPreferenceModelList = list
                callback(list)
                hideProgress()
            }.addOnFailureListener {
                hideProgress()
            }.addOnCanceledListener {
                hideProgress()
            }
    }

    fun getAllDeals(callback: (ArrayList<DealsModel>) -> Unit) {
        showProgress()
        mFirestore.collection("Deals")
            .get()
            .addOnSuccessListener {
                val list: ArrayList<DealsModel> = ArrayList()
                for (document in it.documents) {
                    document.toObject<DealsModel>()
                        ?.let { it1 -> list.add(it1) }
                }
                dealsList = list
                callback(list)
                hideProgress()
            }.addOnFailureListener {
                hideProgress()
            }.addOnCanceledListener {
                hideProgress()
            }
    }

    fun addUserCardPreferences(
        cardPreferenceModel: UserCardPreferenceModel,
        callback: (String) -> Unit
    ) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("CardsPreferences")
                .document(cardPreferenceModel.id)
                .set(cardPreferenceModel)
                .addOnSuccessListener {
                    callback("Saved")
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                    callback("Error")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Cancel")
                }
        }
    }

    fun deleteUserCardPreference(
        userCardPreferenceModel: UserCardPreferenceModel,
        callback: (String) -> Unit
    ) {
        showProgress()
        auth.currentUser?.uid?.let { userId ->
            mFirestore.collection("UserPreferences")
                .document(userId)
                .collection("CardsPreferences")
                .document(userCardPreferenceModel.id)
                .delete()
                .addOnSuccessListener {
                    callback("Deleted")
                    hideProgress()
                }.addOnFailureListener {
                    hideProgress()
                    callback("Error")
                }.addOnCanceledListener {
                    hideProgress()
                    callback("Cancel")
                }
        }
    }
}
