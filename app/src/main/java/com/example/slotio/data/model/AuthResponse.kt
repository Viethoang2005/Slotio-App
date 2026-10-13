package com.example.slotio.data.model

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val user: User?
)
