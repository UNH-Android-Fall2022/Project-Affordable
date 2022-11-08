package com.affordable.ui.main.preferences

import android.view.View
import android.view.WindowManager
import com.affordable.base.BaseActivity
import com.affordable.databinding.ActivityPreferencesBinding

class PreferencesActivity : BaseActivity<ActivityPreferencesBinding>() {

    private val TAG = PreferencesActivity::class.java.name

    override fun initBindingRef(): ActivityPreferencesBinding =
        ActivityPreferencesBinding.inflate(layoutInflater)

    override fun initRoot(): View? = binding?.root

    override fun init() {
        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    override fun initListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}