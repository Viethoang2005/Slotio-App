package com.example.slotio.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val user: User?,
    val token: String?,
    @SerializedName("access_token")
    val accessToken: String?
)
