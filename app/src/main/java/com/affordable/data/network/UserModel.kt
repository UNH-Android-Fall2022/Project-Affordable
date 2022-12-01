package com.affordable.data.network

data class UserModel(
    val username: String = "",
    val firstName:String = "",
    val lastName: String = "",
    val dob: String = "",
    val userId: String = "",
    val userEmail: String = "",
){
    override fun toString(): String {
        return "UserModel(username='$username', firstName='$firstName', lastName='$lastName', dob='$dob', userId='$userId', userEmail='$userEmail')"
    }
}