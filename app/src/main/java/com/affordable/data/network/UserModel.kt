package com.affordable.data.network

data class UserModel(
    val firstName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val password: String = "",
    val isActive: Boolean = true
)