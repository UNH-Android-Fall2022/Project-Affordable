package com.affordable.ui.start

import android.view.View
import com.affordable.base.BaseActivity
import com.affordable.databinding.ActivityStartBinding
import com.affordable.utility.updateWindow

class StartActivity : BaseActivity<ActivityStartBinding>() {

    private val TAG = StartActivity::class.java.name

    override fun initBindingRef(): ActivityStartBinding =
        ActivityStartBinding.inflate(layoutInflater)

    override fun initRoot(): View? = binding?.root

    override fun init() {
        activity.updateWindow()

    }

    override fun initListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}