package com.affordable.data.models

import com.affordable.data.network.ShoppingPreferenceModel
import com.affordable.data.network.StoresPreferenceModel
import com.affordable.data.network.UserCardPreferenceModel

data class MyDealModel(
    val dealId: String = "",
    val dealDetails: String = "",
    val type: String = "",
    val shoppingPreferenceModel: ShoppingPreferenceModel = ShoppingPreferenceModel(),
    val storesPreferenceModel: StoresPreferenceModel = StoresPreferenceModel(),
    val userCardPreferenceModel: UserCardPreferenceModel = UserCardPreferenceModel(),
)