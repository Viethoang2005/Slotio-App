package com.example.slotio.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slotio.data.model.AuthRequest
import com.example.slotio.data.model.UserSession
import com.example.slotio.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = authRepository.login(AuthRequest(email = email, password = password))
                if(response.success) {
                    response.user?.let { user->
                        UserSession.userId = user.id
                        UserSession.userName = user.name
                        UserSession.userEmail = user.email
                    }
                    val tokenStr = (response.token ?: response.accessToken ?: "").removePrefix("Bearer ").trim()
                    if (tokenStr.isNotBlank()) {
                        UserSession.token = tokenStr
                    }
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = response.message) }
                }
            }catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Loi ket noi hoac tai khoan khong ton tai") }
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = authRepository.register(AuthRequest(name = name, email = email, password = password))
                if(response.success) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = response.message) }
                }
            }catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Lỗi kết nối hoặc máy chủ: ${e.localizedMessage}") }
            }
        }
    }

}