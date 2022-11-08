package com.affordable.ui.main.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.base.BaseFragment
import com.affordable.data.models.CardsModel
import com.affordable.databinding.FragmentCardsBinding
import com.affordable.ui.main.preferences.cardSelection.CardsRecyclerviewAdapter


class CardsFragment : BaseFragment<FragmentCardsBinding>() {

    private val TAG = CardsFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCardsBinding = FragmentCardsBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {

            val recyclerviewAdapter = CardsRecyclerviewAdapter(requireContext(),
                object : CardsRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(story: CardsModel) {
                    }

                })

           // recyclerviewAdapter.setData(getCardsDataList())

            cardsRecyclerview.adapter = recyclerviewAdapter

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getCardsDataList(): ArrayList<CardsModel> {
        var dataList = ArrayList<CardsModel>()
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        dataList.add(CardsModel(1, "", "", false))
        return dataList
    }
}