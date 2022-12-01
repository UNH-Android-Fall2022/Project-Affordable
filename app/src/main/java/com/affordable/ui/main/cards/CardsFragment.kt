package com.affordable.ui.main.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.network.StoresPreferenceModel
import com.affordable.data.network.UserCardPreferenceModel
import com.affordable.databinding.FragmentCardsBinding
import com.affordable.ui.main.preferences.cardSelection.CardSelectionFragmentDirections
import com.affordable.ui.main.stores.StoresFragmentDirections
import com.affordable.utility.isNav


class CardsFragment : BaseFragment<FragmentCardsBinding>() {

    private val TAG = CardsFragment::class.java.name

    lateinit var activity: AppCompatActivity

    lateinit var cardPreferenceRecyclerviewAdapter: CardPreferenceRecyclerviewAdapter

    private var originalUserCardsPrefList: ArrayList<UserCardPreferenceModel> = ArrayList()

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCardsBinding = FragmentCardsBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        fetchCardsPrefernces()

    }

    private fun fetchCardsPrefernces() {
        getUserCardsPreferences {
            originalUserCardsPrefList = it
            cardPreferenceRecyclerviewAdapter.setData(originalUserCardsPrefList)
        }
    }


    override fun initListener() {

        with(binding!!) {

            cardPreferenceRecyclerviewAdapter = CardPreferenceRecyclerviewAdapter(requireContext(),
                object : CardPreferenceRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(model: UserCardPreferenceModel, position: Int) {
                        deleteUserCardPreference(model) {
                            showToastShort(it)
                            filterItems.setQuery("",false)
                        }
                        originalUserCardsPrefList.remove(model)
                        cardPreferenceRecyclerviewAdapter.setData(
                            originalUserCardsPrefList
                        )
                    }

                })


            cardsRecyclerview.adapter = cardPreferenceRecyclerviewAdapter

            fab.setOnClickListener {
                navController.isNav(R.id.cardsFragment) {
                    navController.navigate(
                        CardsFragmentDirections.actionCardsFragmentToCardSelectionFragment(
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

        cardPreferenceRecyclerviewAdapter.setData(performFiltering(newText))

    }

    fun performFiltering(constraint: CharSequence?): ArrayList<UserCardPreferenceModel> {

        val charSearch = constraint.toString()

        if (charSearch.isEmpty()) {
            return originalUserCardsPrefList
        } else {

            val resultList = ArrayList<UserCardPreferenceModel>()

            originalUserCardsPrefList.forEach {
                if (it.name.contains(constraint.toString(),ignoreCase = true)) {
                    resultList.add(it)
                }
            }

            return resultList
        }

    }

    private fun getCardsDataList(): ArrayList<UserCardPreferenceModel> {
        var dataList = ArrayList<UserCardPreferenceModel>()
        dataList.add(UserCardPreferenceModel("1", R.drawable.img_alibaba.toString(), ""))
        dataList.add(UserCardPreferenceModel("1", R.drawable.img_bestbuy.toString(), ""))
        dataList.add(UserCardPreferenceModel("1", R.drawable.img_amazon.toString(), ""))
        return dataList
    }
}