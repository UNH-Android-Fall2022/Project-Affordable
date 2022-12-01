package com.affordable.ui.main.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.network.ShoppingPreferenceModel
import com.affordable.databinding.FragmentShoppingBinding
import com.affordable.utility.isNav


class ShoppingFragment : BaseFragment<FragmentShoppingBinding>() {

    private val TAG = ShoppingFragment::class.java.name

    lateinit var activity: AppCompatActivity

    lateinit var shoppingPreferencesRecyclerviewAdapter: ShoppingPreferencesRecyclerviewAdapter

    private var originalUserShoppingPrefList: ArrayList<ShoppingPreferenceModel> = ArrayList()

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentShoppingBinding = FragmentShoppingBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        fetchShoppingPrefernces()
    }

    private fun fetchShoppingPrefernces() {
        getUserShoppingPreferences {
            originalUserShoppingPrefList = it
            shoppingPreferencesRecyclerviewAdapter.setData(originalUserShoppingPrefList)
        }
    }


    override fun initListener() {

        with(binding!!) {

            shoppingPreferencesRecyclerviewAdapter =
                ShoppingPreferencesRecyclerviewAdapter(activity,
                    object : ShoppingPreferencesRecyclerviewAdapter.OnPositionClick {
                        override fun onItemClick(model: ShoppingPreferenceModel, position: Int) {
                            deleteUserShoppingPreference(model) {
                                showToastShort(it)
                                filterItems.setQuery("",false)
                            }
                            originalUserShoppingPrefList.remove(model)
                            shoppingPreferencesRecyclerviewAdapter.setData(
                                originalUserShoppingPrefList
                            )
                        }

                    })

            shoppingsRecyclerview.adapter = shoppingPreferencesRecyclerviewAdapter

            fab.setOnClickListener {
                navController.isNav(R.id.shoppingFragment) {
                    navController.navigate(
                        ShoppingFragmentDirections.actionShoppingFragmentToShoppingSelectionFragment(
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

    private fun filterPlease(newText: String?) {

        shoppingPreferencesRecyclerviewAdapter.setData(performFiltering(newText))

    }

    fun performFiltering(constraint: CharSequence?): ArrayList<ShoppingPreferenceModel> {

        val charSearch = constraint.toString()

        if (charSearch.isEmpty()) {
            return originalUserShoppingPrefList
        } else {

            val resultList = ArrayList<ShoppingPreferenceModel>()

            originalUserShoppingPrefList.forEach {
                if (it.name.contains(constraint.toString(),ignoreCase = true)) {
                    resultList.add(it)
                }
            }

            return resultList
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}