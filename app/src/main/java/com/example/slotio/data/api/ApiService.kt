package com.example.slotio.data.api

import com.example.slotio.data.model.AuthRequest
import com.example.slotio.data.model.AuthResponse
import com.example.slotio.data.model.FavoriteResponse
import com.example.slotio.data.model.Sport
import com.example.slotio.data.model.Venue
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("sports")
    suspend fun getSports() : List<Sport>

    @GET("venues")
    suspend fun getVenues(
        @Query("search") query: String? = null,
        @Query("sport_id") sportId: Int? = null
    ) : List<Venue>

    @POST("login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: AuthRequest) : AuthResponse

    @GET("customer/favorites")
    suspend fun getFavoriteVenues(@Header("Authorization") token: String) : Response<FavoriteResponse>

    @POST("customer/favorites")
    suspend fun addFavoriteVenue(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    )

    @DELETE("customer/favorites/{venueId}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("venueId") venueId: Int
    )
}