package com.affordable.ui.main.stores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.network.ShoppingPreferenceModel
import com.affordable.data.network.StoresPreferenceModel
import com.affordable.databinding.FragmentStoresBinding
import com.affordable.ui.main.shopping.ShoppingFragmentDirections
import com.affordable.utility.isNav


class StoresFragment : BaseFragment<FragmentStoresBinding>() {

    private val TAG = StoresFragment::class.java.name

    lateinit var activity: AppCompatActivity

    lateinit var storesPreferencesRecyclerviewAdapter: StoresPreferencesRecyclerviewAdapter

    private var originalUserShoppingtoresPrefList: ArrayList<StoresPreferenceModel> = ArrayList()

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentStoresBinding = FragmentStoresBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        fetchStoresPrefernces()

    }

    private fun fetchStoresPrefernces() {
        getUserStoresPreferences {
            originalUserShoppingtoresPrefList = it
            storesPreferencesRecyclerviewAdapter.setData(originalUserShoppingtoresPrefList)
        }
    }

    override fun initListener() {

        with(binding!!) {

            storesPreferencesRecyclerviewAdapter = StoresPreferencesRecyclerviewAdapter(activity,
                object : StoresPreferencesRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(model: StoresPreferenceModel,position:Int) {
                        deleteUserStorePreference(model) {
                            showToastShort(it)
                            filterItems.setQuery("",false)
                        }
                        originalUserShoppingtoresPrefList.remove(model)
                        storesPreferencesRecyclerviewAdapter.setData(
                            originalUserShoppingtoresPrefList
                        )
                    }

                })

            shoppingsRecyclerview.adapter = storesPreferencesRecyclerviewAdapter

            fab.setOnClickListener {
                navController.isNav(R.id.storesFragment) {
                    navController.navigate(
                        StoresFragmentDirections.actionStoresFragmentToChoiceSelectionFragment(
                            "Not New"
                        )
                    )
                }
            }

            filterItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    filterPlease(newText)

                    return false
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun filterPlease(newText: String?) {

        storesPreferencesRecyclerviewAdapter.setData(performFiltering(newText))

    }

    fun performFiltering(constraint: CharSequence?): ArrayList<StoresPreferenceModel> {

        val charSearch = constraint.toString()

        if (charSearch.isEmpty()) {
            return originalUserShoppingtoresPrefList
        } else {

            val resultList = ArrayList<StoresPreferenceModel>()

            originalUserShoppingtoresPrefList.forEach {
                if (it.name.contains(constraint.toString(),ignoreCase = true)) {
                    resultList.add(it)
                }
            }

            return resultList
        }

    }

    private fun getCardsDataList(): ArrayList<StoresPreferenceModel> {
        var dataList = ArrayList<StoresPreferenceModel>()
        dataList.add(StoresPreferenceModel("1", R.drawable.img_bestbuy.toString(), ""))
        dataList.add(StoresPreferenceModel("1", R.drawable.img_alibaba.toString(), ""))
        dataList.add(StoresPreferenceModel("1", R.drawable.img_amazon.toString(), ""))
        return dataList
    }

}