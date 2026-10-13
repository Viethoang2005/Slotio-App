package com.example.slotio.data.repository

import com.example.slotio.data.api.ApiService
import com.example.slotio.data.model.Venue
import com.google.gson.JsonObject
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

    suspend fun removeFavorite(token: String, venueId: Int) {
        return api.removeFavorite(token, venueId)
    }

    suspend fun addFavorite(token: String, venueId: Int) {
        val body = JsonObject().apply {
            addProperty("venue_id", venueId)
        }
        return api.addFavoriteVenue(token, body)
    }
    suspend fun getFavoriteVenues(token: String) : retrofit2.Response<com.example.slotio.data.model.FavoriteResponse> {
        return api.getFavoriteVenues(token)
    }
}