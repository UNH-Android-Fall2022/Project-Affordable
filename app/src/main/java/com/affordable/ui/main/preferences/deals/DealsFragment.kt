package com.affordable.ui.main.preferences.deals

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.models.MyDealModel
import com.affordable.data.network.ShoppingPreferenceModel
import com.affordable.data.network.StoresPreferenceModel
import com.affordable.data.network.UserCardPreferenceModel
import com.affordable.databinding.FragmentDealsBinding
import com.affordable.utility.Utils
import com.affordable.utility.isNav


class DealsFragment : BaseFragment<FragmentDealsBinding>() {

    private val TAG = DealsFragment::class.java.name

    lateinit var activity: AppCompatActivity

    private lateinit var dealsRecyclerViewAdapter: DealsRecyclerViewAdapter

    private var userCardPreferenceModelList: ArrayList<UserCardPreferenceModel> = ArrayList()

    private var userShoppingPreferenceModelList: ArrayList<ShoppingPreferenceModel> = ArrayList()

    private var userStoresPreferenceModelList: ArrayList<StoresPreferenceModel> = ArrayList()

    val args: DealsFragmentArgs by navArgs()

    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDealsBinding =
        FragmentDealsBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

    }

    override fun initListener() {

        with(binding!!) {

            if (args.type.equals("New")) {
                logo.visibility = View.VISIBLE
                thanksText.visibility = View.VISIBLE
                newLayout.visibility = View.VISIBLE
            } else {
                logo.visibility = View.GONE
                thanksText.visibility = View.GONE
                newLayout.visibility = View.GONE
                totalDealsText.text = ""
            }

            exploreMore.setOnClickListener {
                navController.isNav(R.id.thanksFragment) {
                    navController.navigate(DealsFragmentDirections.actionThanksFragmentToMainActivity2())
                    activity.finish()
                }
            }

            next.setOnClickListener {
                navController.isNav(R.id.thanksFragment) {
                    navController.navigate(DealsFragmentDirections.actionThanksFragmentToMainActivity2())
                    activity.finish()
                }
            }

            dealsRecyclerViewAdapter = DealsRecyclerViewAdapter(requireContext(),
                object : DealsRecyclerViewAdapter.OnPositionClick {
                    override fun onItemClick(model: MyDealModel, position: Int) {

                    }

                })

            dealsRecyclerview.adapter = dealsRecyclerViewAdapter

            fetchDealsAndUserPreferences()
        }
    }

    private fun fetchDealsAndUserPreferences() {

        showMyProgressbar()

        fetchAllStoresCategoriesCards {
            fetchAllUserStoresCategoreisCards {
                filterUserPreferenceDeals()
            }
        }


    }

    private fun fetchAllUserStoresCategoreisCards(callback: () -> Unit) {
        getUserCardsPreferences { userCardsList ->
            userCardPreferenceModelList = userCardsList
            getUserStoresPreferences { userStoresList ->
                userStoresPreferenceModelList = userStoresList
                getUserShoppingPreferences { userShoppingList ->
                    userShoppingPreferenceModelList = userShoppingList
                    callback()
                }
            }
        }
    }

    private fun filterUserPreferenceDeals() {

        var allUserDealsList = ArrayList<MyDealModel>()

        userCardPreferenceModelList.forEach { userCard ->
            val deal = dealsList.find { it.CardId.trim() == userCard.id.trim() }
            if (deal != null) {
                val category =
                    shoppingPreferenceModelList.find { it.id.trim() == deal.CategoryId.trim() }
                val store = storesPreferenceModelList.find { it.id.trim() == deal.StoreId.trim() }
                val card = userCard
                val myDealModel = MyDealModel(
                    dealId = deal.DealId,
                    dealDetails = deal.Details,
                    userCardPreferenceModel = card,
                    shoppingPreferenceModel = if (category != null) category else ShoppingPreferenceModel(),
                    storesPreferenceModel = if (store != null) store else StoresPreferenceModel(),
                    type = Utils.CARD_PREFERENCE
                )
                allUserDealsList.add(myDealModel)
            }
        }

        userStoresPreferenceModelList.forEach { userStore ->
            val deal = dealsList.find { it.StoreId.trim() == userStore.id.trim() }
            if (deal != null) {
                val category =
                    shoppingPreferenceModelList.find { it.id.trim() == deal.CategoryId.trim() }
                val store = userStore
                val card = cardsPreferenceModelList.find { it.id.trim() == deal.CardId.trim() }
                val myDealModel = MyDealModel(
                    dealId = deal.DealId,
                    dealDetails = deal.Details,
                    userCardPreferenceModel = if (card != null) card else UserCardPreferenceModel(),
                    shoppingPreferenceModel = if (category != null) category else ShoppingPreferenceModel(),
                    storesPreferenceModel = store,
                    type = Utils.STORE_PREFERENCE
                )
                allUserDealsList.add(myDealModel)
            }
        }

        userShoppingPreferenceModelList.forEach { userShopCategory ->
            val deal = dealsList.find { it.CategoryId.trim() == userShopCategory.id.trim() }
            if (deal != null) {
                val category = userShopCategory
                val store = storesPreferenceModelList.find { it.id.trim() == deal.StoreId.trim() }
                val card = cardsPreferenceModelList.find { it.id.trim() == deal.CardId.trim() }
                val myDealModel = MyDealModel(
                    dealId = deal.DealId,
                    dealDetails = deal.Details,
                    shoppingPreferenceModel = category,
                    userCardPreferenceModel = if (card != null) card else UserCardPreferenceModel(),
                    storesPreferenceModel = if (store != null) store else StoresPreferenceModel(),
                    type = Utils.SHOPPING_PREFERENCE
                )
                allUserDealsList.add(myDealModel)
            }
        }

        allUserDealsList =
            allUserDealsList.distinctBy { it.dealId.trim() } as ArrayList<MyDealModel>

        dealsRecyclerViewAdapter.setData(allUserDealsList)

        setTotalDealsText(allUserDealsList)

        hideMyProgressbar()

    }

    private fun setTotalDealsText(allUserDealsList: java.util.ArrayList<MyDealModel>) {
        binding?.totalDealsText?.text =
            "Affordable found ${allUserDealsList.size} deals based on your preferences"
    }

    private fun showMyProgressbar() {
        binding?.loading?.visibility = View.VISIBLE
    }

    private fun hideMyProgressbar() {
        binding?.loading?.visibility = View.GONE
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}