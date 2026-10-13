package com.example.slotio.data.api

import com.example.slotio.data.model.AuthRequest
import com.example.slotio.data.model.AuthResponse
import com.example.slotio.data.model.Sport
import com.example.slotio.data.model.Venue
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}