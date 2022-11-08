package com.affordable.ui.main.feedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentFeedbackBinding

class FeedbackFragment : BaseFragment<FragmentFeedbackBinding>() {

    private val TAG = FeedbackFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFeedbackBinding = FragmentFeedbackBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}