package com.affordable.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.base.BaseFragment
import com.affordable.data.models.RecommendationModel
import com.affordable.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val TAG = HomeFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {
            val recyclerviewAdapter = RecommandationRecyclerviewAdapter(requireContext(),
                object : RecommandationRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(story: RecommendationModel) {
                    }

                })

            recyclerviewAdapter.setData(getDataList())

            recommendationRecyclerview.adapter = recyclerviewAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getDataList(): ArrayList<RecommendationModel> {
        var dataList = ArrayList<RecommendationModel>()
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        dataList.add(RecommendationModel(1, "", ""))
        return dataList
    }
}