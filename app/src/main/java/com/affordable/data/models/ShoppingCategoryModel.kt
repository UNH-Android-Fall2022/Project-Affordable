package com.affordable.data.models

data class ShoppingCategoryModel(
    val categoryId: String,
    val categoryName: String,
    val categoryDescription: String,
    var isSelected: Boolean
)
