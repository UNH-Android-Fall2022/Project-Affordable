package com.affordable.ui.main.preferences.cardSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.models.CardsModel
import com.affordable.databinding.FragmentCardSelectionBinding
import com.affordable.utility.isNav


class CardSelectionFragment : BaseFragment<FragmentCardSelectionBinding>() {

    private val TAG = CardSelectionFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCardSelectionBinding =
        FragmentCardSelectionBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {

            skip.setOnClickListener {
                navController.isNav(R.id.cardSelectionFragment) {
                    navController.navigate(CardSelectionFragmentDirections.actionCardSelectionFragmentToChoiceSelectionFragment())
                }
            }

            next.setOnClickListener {
                navController.isNav(R.id.cardSelectionFragment) {
                    navController.navigate(CardSelectionFragmentDirections.actionCardSelectionFragmentToChoiceSelectionFragment())
                }
            }


            val recyclerviewAdapter = CardsRecyclerviewAdapter(requireContext(),
                object : CardsRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(story: CardsModel) {
                    }

                })

            recyclerviewAdapter.setData(getCardsDataList())

            cardsRecyclerview.adapter = recyclerviewAdapter
        }
    }

    private fun getCardsDataList(): ArrayList<CardsModel> {
        var dataList = ArrayList<CardsModel>()
        dataList.add(CardsModel(1, "", "", R.drawable.visa_img, false))
        dataList.add(CardsModel(1, "", "", R.drawable.img_master, false))
        dataList.add(CardsModel(1, "", "", R.drawable.img_american, false))
        return dataList
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}