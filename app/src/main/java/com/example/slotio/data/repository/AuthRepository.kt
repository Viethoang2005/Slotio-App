package com.example.slotio.data.repository

import com.example.slotio.data.api.ApiService
import com.example.slotio.data.model.AuthRequest
import com.example.slotio.data.model.AuthResponse
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun login(request: AuthRequest) : AuthResponse {
        return api.login(request)
    }

    suspend fun register(request: AuthRequest) : AuthResponse {
        return api.register(request)
    }
}