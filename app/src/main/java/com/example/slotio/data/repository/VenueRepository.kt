package com.example.slotio.data.repository

import com.example.slotio.data.api.ApiService
import com.example.slotio.data.model.Venue
import javax.inject.Inject

class VenueRepository @Inject constructor(
    private val api: ApiService
){

    suspend fun searchVenues(query: String) : List<Venue> {
        return api.getVenues(query = query)
    }

    suspend fun getVenues(sportId: Int? = null) : List<Venue> {
        return api.getVenues(sportId = sportId)
    }

}