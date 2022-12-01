package com.affordable.ui.main.preferences.cardSelection

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent
import com.affordable.R
import com.affordable.base.BaseFragment
import com.affordable.data.models.CardValidator
import com.affordable.data.models.CardsModel
import com.affordable.data.network.UserCardPreferenceModel
import com.affordable.databinding.FragmentCardSelectionBinding
import com.affordable.utility.isNav
import java.util.regex.Pattern


class CardSelectionFragment : BaseFragment<FragmentCardSelectionBinding>() {

    private val TAG = CardSelectionFragment::class.java.name

    lateinit var activity: AppCompatActivity

    lateinit var cardsRecyclerviewAdapter: CardsRecyclerviewAdapter

    val args: CardSelectionFragmentArgs by navArgs()

    val REQUEST_CODE_SCAN_CARD = 1


    override fun initBindingRef(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCardSelectionBinding =
        FragmentCardSelectionBinding.inflate(inflater, container, false)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        activity = requireActivity() as AppCompatActivity

        fetchCards()

    }

    private fun fetchCards() {
        getAllCardsPreferences {
            cardsRecyclerviewAdapter.setData(convertAndUpdate(it, selectAll = false))
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
                cardsRecyclerviewAdapter.setData(
                    convertAndUpdate(
                        cardsPreferenceModelList,
                        selectAll = true
                    )
                )
                saveUserPreferences()
            }

            add.setOnClickListener {
                saveUserPreferences()
            }

            skip.setOnClickListener {
                navController.isNav(R.id.cardSelectionFragment) {
                    navController.navigate(
                        CardSelectionFragmentDirections.actionCardSelectionFragmentToChoiceSelectionFragment(
                            "New"
                        )
                    )
                }
            }

            next.setOnClickListener {
                saveUserPreferences()
            }


            cardsRecyclerviewAdapter = CardsRecyclerviewAdapter(requireContext(),
                object : CardsRecyclerviewAdapter.OnPositionClick {
                    override fun onItemClick(model: CardsModel, position: Int) {
                        cardsRecyclerviewAdapter.notifyItemChanged(position, model)
                    }

                })


            cardsRecyclerview.adapter = cardsRecyclerviewAdapter

            scanCard.setOnClickListener {
                val intent = ScanCardIntent.Builder(activity).build()
                startActivityForResult(intent, REQUEST_CODE_SCAN_CARD)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE_SCAN_CARD) {
            if (resultCode === Activity.RESULT_OK) {
                val card: Card? = data?.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD)
                val cardData = """
            Card number: ${card?.cardNumber.toString()}
            Card holder: ${card?.cardHolderName.toString()}
            Card expiration date: ${card?.expirationDate.toString()}
            """.trimIndent()
                Log.i(TAG, "Card info: $cardData")
                saveCard(card)
            } else if (resultCode === Activity.RESULT_CANCELED) {
                Log.i(TAG, "Scan canceled")
            } else {
                Log.i(TAG, "Scan failed")
            }
        }
    }

    private fun saveCard(card: Card?) {
        val cardName = getCardType(card?.cardNumber.toString())?.cardName
        var founded = false
        cardsPreferenceModelList.forEach { myCard ->
            if (myCard.name.equals(cardName, ignoreCase = true)) {
                founded = true
                addUserCardPreferences(myCard) {
                    showToastShort(cardName+" Card " + it)
                }
            }
        }
        if(!founded){
            showToastShort("Card Not Exist")
        }
    }


    fun sanitizeEntry(entry: String, isNumber: Boolean): String {
        return if (isNumber) entry.replace(
            "\\D".toRegex(),
            ""
        ) else entry.replace("\\s+|-".toRegex(), "")
    }


    fun getCardType(num: String): CardValidator? {
        var num = num
        num = sanitizeEntry(num, true)
        if (Pattern.matches("^(54)", num) && num.length > 16) {
            return CardValidator.MAESTRO
        }
        val cards: Array<CardValidator> = CardValidator.values()
        for (i in cards.indices) {
            if (Pattern.matches(cards[i].cardPattern, num)) {
                return cards[i]
            }
        }
        return null
    }

    private fun convertAndUpdate(
        shoppingPreferenceModels: ArrayList<UserCardPreferenceModel>,
        selectAll: Boolean = false
    ): ArrayList<CardsModel> {
        var dataList = ArrayList<CardsModel>()
        shoppingPreferenceModels.forEach { store ->
            dataList.add(
                CardsModel(
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
        val list = cardsRecyclerviewAdapter.mainList
        if (list.firstOrNull { it.isSelected == true } == null) {
            showToastShort("No Item Selected")
        } else {
            list.forEach { shopItem ->
                if (shopItem.isSelected) {
                    addUserCardPreferences(cardsPreferenceModelList.first { it.id == shopItem.cardId }) {

                    }
                }
            }
            showToastShort("Cards Added Successfully")

            if (args.type.equals("Not New")) {
                activity.onBackPressed();
            } else {
                navController.isNav(R.id.cardSelectionFragment) {
                    navController.navigate(
                        CardSelectionFragmentDirections.actionCardSelectionFragmentToChoiceSelectionFragment(
                            "New"
                        )
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}