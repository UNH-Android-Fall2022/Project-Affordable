package com.affordable.ui.main.preferences.shoppingSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.models.ShoppingCategoryModel
import com.affordable.data.network.ShoppingPreferenceModel
import com.affordable.databinding.FragmentShoppingSelectionBinding
import com.affordable.utility.isNav


class ShoppingSelectionFragment : BaseFragment<FragmentShoppingSelectionBinding>() {

    private val TAG = ShoppingSelectionFragment::class.java.name

    lateinit var activity: AppCompatActivity

    lateinit var shoppingCategoryRecyclerviewAdapter: ShoppingCategoryRecyclerviewAdapter

    val args: ShoppingSelectionFragmentArgs by navArgs()

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentShoppingSelectionBinding =
        FragmentShoppingSelectionBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        fetchShoppingCategories()

    }

    private fun fetchShoppingCategories() {
        getAllShoppingPreferences {
            shoppingCategoryRecyclerviewAdapter.setData(convertAndUpdate(it, selectAll = false))
        }
    }


    override fun initListener() {

        with(binding!!) {

            if (args.type.equals("Not New")) {
                imageView.visibility = View.GONE
                newLayout.visibility = View.GONE
                oldLayout.visibility = View.VISIBLE
            }

            selectAllAndAdd.setOnClickListener {
                shoppingCategoryRecyclerviewAdapter.setData(
                    convertAndUpdate(
                        shoppingPreferenceModelList,
                        selectAll = true
                    )
                )
                saveUserPreferences()
            }

            add.setOnClickListener {
                saveUserPreferences()
            }

            selectAllNext.setOnClickListener {
                shoppingCategoryRecyclerviewAdapter.setData(
                    convertAndUpdate(
                        shoppingPreferenceModelList,
                        selectAll = true
                    )
                )
                saveUserPreferences()
            }

            next.setOnClickListener {
                saveUserPreferences()
            }


            shoppingCategoryRecyclerviewAdapter =
                ShoppingCategoryRecyclerviewAdapter(requireContext(),
                    object : ShoppingCategoryRecyclerviewAdapter.OnPositionClick {
                        override fun onItemClick(model: ShoppingCategoryModel, position: Int) {
                            shoppingCategoryRecyclerviewAdapter.notifyItemChanged(position, model)
                        }

                    })

            shoppingsRecyclerview.adapter = shoppingCategoryRecyclerviewAdapter
        }
    }

    private fun saveUserPreferences() {
        val list = shoppingCategoryRecyclerviewAdapter.mainList
        if (list.firstOrNull { it.isSelected == true } == null) {
            showToastShort("No Item Selected")
        } else {
            list.forEach { shopItem ->
                if (shopItem.isSelected) {
                    addUserShoppingPreferences(shoppingPreferenceModelList.first { it.id == shopItem.categoryId }) {

                    }
                }
            }
            showToastShort("Category Added Successfully")
            if (args.type.equals("Not New")) {
                activity.onBackPressed();
            }else{
                navController.isNav(R.id.shoppingSelectionFragment) {
                    navController.navigate(ShoppingSelectionFragmentDirections.actionShoppingSelectionFragmentToThanksFragment())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun convertAndUpdate(
        shoppingPreferenceModels: ArrayList<ShoppingPreferenceModel>,
        selectAll: Boolean = false
    ): ArrayList<ShoppingCategoryModel> {
        var dataList = ArrayList<ShoppingCategoryModel>()
        shoppingPreferenceModels.forEach { shoppingModel ->
            dataList.add(
                ShoppingCategoryModel(
                    shoppingModel.id,
                    shoppingModel.name,
                    shoppingModel.description,
                    selectAll
                )
            )
        }
        return dataList
    }
}