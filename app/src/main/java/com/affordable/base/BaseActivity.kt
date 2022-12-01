package com.affordable.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R


abstract class BaseActivity<VB> : AppCompatActivity() {

    private var mDialog: Dialog? = null
    private lateinit var mContext: Context
    protected val context: Context get() = mContext

    private lateinit var mActivity: AppCompatActivity
    protected val activity: AppCompatActivity get() = mActivity

    private var mBinding: VB? = null
    protected val binding: VB? get() = mBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mActivity = this
        mBinding = initBindingRef()
        setContentView(initRoot())
        init()
        initListener()

    }

    protected abstract fun initBindingRef(): VB
    protected abstract fun initRoot(): View?
    protected abstract fun init()
    protected abstract fun initListener()

    fun showProgress() {
        // no tile for the dialog
        if (mDialog != null && mDialog!!.isShowing()) {
            mDialog!!.dismiss()
            mDialog = null
        }
        mDialog = Dialog(this)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.progress_bar_dialog)
        mDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mProgressBar: ProgressBar = mDialog!!.findViewById<ProgressBar>(R.id.progress_bar)
        mProgressBar.visibility = View.VISIBLE
        mProgressBar.isIndeterminate = true
        mDialog!!.setCancelable(false)
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
    }

    open fun hideProgress() {
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}