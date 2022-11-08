package com.affordable.ui.main.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.base.BaseFragment
import com.affordable.databinding.FragmentAccountBinding


class AccountFragment : BaseFragment<FragmentAccountBinding>() {

    private val TAG = AccountFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAccountBinding = FragmentAccountBinding.inflate(inflater, container, false)

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