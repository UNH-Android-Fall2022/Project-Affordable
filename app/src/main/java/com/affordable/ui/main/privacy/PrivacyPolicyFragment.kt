package com.affordable.ui.main.privacy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentPrivacyPolicyBinding


class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding>() {

    private val TAG = PrivacyPolicyFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPrivacyPolicyBinding =
        FragmentPrivacyPolicyBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {

            getPrivacyPolicy({ privacy ->
                policy.text = privacy.data.replace(".  ", "\n\n")
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}