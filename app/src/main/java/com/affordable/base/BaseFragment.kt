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
import com.affordable.utility.NetworkHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


abstract class BaseFragment<VB> : Fragment() {

    private var mDialog: Dialog? = null
    private var mBinding: VB? = null
    protected val binding: VB? get() = mBinding

    lateinit var mNavController: NavController
    protected open val navController: NavController get() = mNavController

    protected lateinit var mFirestore: FirebaseFirestore
    protected lateinit var auth: FirebaseAuth

    protected lateinit var networkHelper: NetworkHelper

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
            if (requireActivity().currentFocus != null)
                inputMethodManager.hideSoftInputFromWindow(
                    requireActivity().currentFocus!!.windowToken,
                    0
                )
        }
        if (view.hasFocus())
            view.clearFocus()
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

}
