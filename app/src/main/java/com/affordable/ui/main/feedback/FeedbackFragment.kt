package com.affordable.ui.main.feedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentFeedbackBinding
import com.hadi.emojiratingbar.RateStatus

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

            sendFeedback.setOnClickListener {
                if(edtYourFeedback.text.toString().trim().isEmpty()){
                    showToastShort("Please write feedback")
                }else{
                    submitFeedback(emojiRatingBar.getCurrentRateStatus().toString(),edtYourFeedback.text.toString().trim()){
                        showToastShort(it)
                        emojiRatingBar.setCurrentRateStatus(RateStatus.OKAY)
                        edtYourFeedback.setText("")
                        edtYourFeedback.clearFocus()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}