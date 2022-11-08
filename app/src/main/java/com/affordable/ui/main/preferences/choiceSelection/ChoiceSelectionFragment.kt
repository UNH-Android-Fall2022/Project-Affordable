package com.affordable.ui.main.preferences.choiceSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.models.ChoiceShoppingModel
import com.affordable.databinding.FragmentChoiceSelectionBinding
import com.affordable.utility.isNav


class ChoiceSelectionFragment : BaseFragment<FragmentChoiceSelectionBinding>() {

    private val TAG = ChoiceSelectionFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentChoiceSelectionBinding =
        FragmentChoiceSelectionBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {

            skip.setOnClickListener {
                navController.isNav(R.id.choiceSelectionFragment) {
                    navController.navigate(ChoiceSelectionFragmentDirections.actionChoiceSelectionFragmentToShoppingSelectionFragment())
                }
            }

            next.setOnClickListener {
                navController.isNav(R.id.choiceSelectionFragment) {
                    navController.navigate(ChoiceSelectionFragmentDirections.actionChoiceSelectionFragmentToShoppingSelectionFragment())
                }
            }


            val recyclerviewAdapter = ChoiceShoppingRecyclerviewAdapter(requireContext(),
                object : ChoiceShoppingRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(story: ChoiceShoppingModel) {
                    }

                })

            recyclerviewAdapter.setData(getCardsDataList())

            shoppingsRecyclerview.adapter = recyclerviewAdapter

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getCardsDataList(): ArrayList<ChoiceShoppingModel> {
        var dataList = ArrayList<ChoiceShoppingModel>()
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        dataList.add(ChoiceShoppingModel(1, "", "", false))
        return dataList
    }
}