package com.affordable.ui.main.preferences.shoppingSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.models.ShoppingCategoryModel
import com.affordable.databinding.FragmentShoppingSelectionBinding
import com.affordable.utility.isNav


class ShoppingSelectionFragment : BaseFragment<FragmentShoppingSelectionBinding>() {

    private val TAG = ShoppingSelectionFragment::class.java.name

    lateinit var activity: AppCompatActivity

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentShoppingSelectionBinding =
        FragmentShoppingSelectionBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity


    }


    override fun initListener() {

        with(binding!!) {


            selectAllNext.setOnClickListener {
                navController.isNav(R.id.shoppingSelectionFragment) {
                    navController.navigate(ShoppingSelectionFragmentDirections.actionShoppingSelectionFragmentToThanksFragment())
                }
            }

            next.setOnClickListener {
                navController.isNav(R.id.shoppingSelectionFragment) {
                    navController.navigate(ShoppingSelectionFragmentDirections.actionShoppingSelectionFragmentToThanksFragment())
                }
            }


            val recyclerviewAdapter = ShoppingCategoryRecyclerviewAdapter(requireContext(),
                object : ShoppingCategoryRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(story: ShoppingCategoryModel) {
                    }

                })

            recyclerviewAdapter.setData(getDataList())

            shoppingsRecyclerview.adapter = recyclerviewAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getDataList(): ArrayList<ShoppingCategoryModel> {
        var dataList = ArrayList<ShoppingCategoryModel>()
        dataList.add(ShoppingCategoryModel(1, "Clothes", "", false))
        dataList.add(ShoppingCategoryModel(1, "Travel", "", false))
        dataList.add(ShoppingCategoryModel(1, "Home", "", false))
        dataList.add(ShoppingCategoryModel(1, "Pharmacy", "", false))
        dataList.add(ShoppingCategoryModel(1, "Sports", "", false))
        dataList.add(ShoppingCategoryModel(1, "Beauty", "", false))
        dataList.add(ShoppingCategoryModel(1, "Movies", "", false))
        dataList.add(ShoppingCategoryModel(1, "Mails", "", false))
        dataList.add(ShoppingCategoryModel(1, "Nike", "", false))
        dataList.add(ShoppingCategoryModel(1, "Puma", "", false))
        dataList.add(ShoppingCategoryModel(1, "Service", "", false))
        dataList.add(ShoppingCategoryModel(1, "Bata", "", false))
        dataList.add(ShoppingCategoryModel(1, "Vegetables", "", false))
        return dataList
    }
}