package com.example.slotio.data.repository

import android.util.Log
import com.example.slotio.data.api.ApiService
import com.example.slotio.data.model.Sport
import javax.inject.Inject

class SportRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getSports() = api.getSports().also {
        Log.d("Sport_API", "Sports: $it")
    }

}