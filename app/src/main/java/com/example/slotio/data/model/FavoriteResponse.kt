package com.example.slotio.data.model

data class FavoriteResponse(
    val success: Boolean,
    val message: String,
    val data: List<Venue>?
)
