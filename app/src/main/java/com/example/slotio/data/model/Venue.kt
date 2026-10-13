package com.example.slotio.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class Venue(
    val id: Int,
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("sport_id")
    val sportId: Int? = null,
    val isFavorite: Boolean = false
)

