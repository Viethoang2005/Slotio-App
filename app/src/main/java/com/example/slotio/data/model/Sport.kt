package com.example.slotio.data.model

import com.google.gson.annotations.SerializedName

data class Sport(
    val id: Int,
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String
)
