package com.affordable.ui.main.preferences.choiceSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.models.ChoiceShoppingModel
import com.affordable.data.network.StoresPreferenceModel
import com.affordable.databinding.FragmentChoiceSelectionBinding
import com.affordable.utility.isNav


class ChoiceSelectionFragment : BaseFragment<FragmentChoiceSelectionBinding>() {

    private val TAG = ChoiceSelectionFragment::class.java.name

    lateinit var activity: AppCompatActivity

    lateinit var choiceSelectionRecyclerviewAdapter: ChoiceSelectionRecyclerviewAdapter

    val args: ChoiceSelectionFragmentArgs by navArgs()

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentChoiceSelectionBinding =
        FragmentChoiceSelectionBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        fetchStoreCategories()
    }

    private fun fetchStoreCategories() {
        getAllStoresPreferences {
            choiceSelectionRecyclerviewAdapter.setData(convertAndUpdate(it, selectAll = false))
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
                choiceSelectionRecyclerviewAdapter.setData(
                    convertAndUpdate(
                        storesPreferenceModelList,
                        selectAll = true
                    )
                )
                saveUserPreferences()
            }

            add.setOnClickListener {
                saveUserPreferences()
            }

            skip.setOnClickListener {
                navController.isNav(R.id.choiceSelectionFragment) {
                    navController.navigate(
                        ChoiceSelectionFragmentDirections.actionChoiceSelectionFragmentToShoppingSelectionFragment(
                            "New"
                        )
                    )
                }
            }

            next.setOnClickListener {
                saveUserPreferences()
            }


            choiceSelectionRecyclerviewAdapter =
                ChoiceSelectionRecyclerviewAdapter(requireContext(),
                    object : ChoiceSelectionRecyclerviewAdapter.OnPositionClick {
                        override fun onItemClick(model: ChoiceShoppingModel, position: Int) {
                            choiceSelectionRecyclerviewAdapter.notifyItemChanged(position, model)
                        }

                    })


            shoppingsRecyclerview.adapter = choiceSelectionRecyclerviewAdapter

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun convertAndUpdate(
        shoppingPreferenceModels: ArrayList<StoresPreferenceModel>,
        selectAll: Boolean = false
    ): ArrayList<ChoiceShoppingModel> {
        var dataList = ArrayList<ChoiceShoppingModel>()
        shoppingPreferenceModels.forEach { store ->
            dataList.add(
                ChoiceShoppingModel(
                    store.id,
                    store.name,
                    store.imageUrl,
                    selectAll
                )
            )
        }
        return dataList
    }

    private fun saveUserPreferences() {
        val list = choiceSelectionRecyclerviewAdapter.mainList
        if (list.firstOrNull { it.isSelected == true } == null) {
            showToastShort("No Item Selected")
        } else {
            list.forEach { shopItem ->
                if (shopItem.isSelected) {
                    addUserStorePreferences(storesPreferenceModelList.first { it.id == shopItem.cardId }) {

                    }
                }
            }
            showToastShort("Shops Added Successfully")

            if (args.type.equals("Not New")) {
                activity.onBackPressed();
            } else {
                navController.isNav(R.id.choiceSelectionFragment) {
                    navController.navigate(
                        ChoiceSelectionFragmentDirections.actionChoiceSelectionFragmentToShoppingSelectionFragment(
                            "New"
                        )
                    )
                }
            }
        }
    }
}