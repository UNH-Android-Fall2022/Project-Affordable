package com.affordable.data.models

data class ChoiceShoppingModel(
    val cardId: Int,
    val cardType: String,
    val cardCVC: String,
    var isSelected: Boolean
)
