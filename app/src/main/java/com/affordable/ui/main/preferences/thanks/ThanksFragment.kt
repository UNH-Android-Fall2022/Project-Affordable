package com.affordable.ui.main.preferences.thanks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentThanksBinding
import com.affordable.utility.isNav


class ThanksFragment : BaseFragment<FragmentThanksBinding>() {

    private val TAG = ThanksFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentThanksBinding =
        FragmentThanksBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {
            exploreMore.setOnClickListener {
                navController.isNav(R.id.thanksFragment) {
                    navController.navigate(ThanksFragmentDirections.actionThanksFragmentToMainActivity2())
                    activity.finish()
                }
            }

            next.setOnClickListener {
                navController.isNav(R.id.thanksFragment) {
                    navController.navigate(ThanksFragmentDirections.actionThanksFragmentToMainActivity2())
                    activity.finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}