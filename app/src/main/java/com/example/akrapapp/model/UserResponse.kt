package com.example.akrapapp.model

data class UserResponse(
    val message: String,
    val data: UserData
)
data class UserData(
    val fullName: String,
    val phoneNumber: String,
    val birthdate: String,
    val gender: String,
    val username: String,
    val role: String,
    val status: Int
)
