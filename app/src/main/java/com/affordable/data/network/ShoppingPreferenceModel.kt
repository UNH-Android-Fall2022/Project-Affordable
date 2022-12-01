package com.affordable.data.network

data class ShoppingPreferenceModel(
    val id: String = "",
    val name: String = "",
    val description: String = ""
){
    override fun toString(): String {
        return "ShoppingPreferenceModel(id='$id', name='$name', description='$description')"
    }
}
