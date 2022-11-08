package com.affordable.data.models

data class ShoppingCategoryModel(
    val categoryId: Int,
    val categoryName: String,
    val categoryType: String,
    var isSelected: Boolean
)
