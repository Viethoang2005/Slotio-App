package com.example.slotio.data.model

data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null
)
