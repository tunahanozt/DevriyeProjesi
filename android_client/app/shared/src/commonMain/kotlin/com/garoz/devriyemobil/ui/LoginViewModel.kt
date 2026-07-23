package com.garoz.devriyemobil.ui

import com.garoz.devriyemobil.network.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun login(sicilNo: String, sifre: String) {
        _uiState.value = LoginState.Loading // UI'a yükleniyor animasyonu göster talimatı

        CoroutineScope(Dispatchers.Default).launch {
            val result = repository.login(sicilNo, sifre)

            result.fold(
                onSuccess = { token ->
                    // Başarılı giriş, Token alındı!
                    _uiState.value = LoginState.Success(token)
                },
                onFailure = { error ->
                    // C# tarafındaki Unauthorized veya bağlantı hatası
                    _uiState.value = LoginState.Error(error.message ?: "Bilinmeyen bir hata oluştu")
                }
            )
        }
    }
}

// Ekranın içinde bulunabileceği 4 farklı durumu temsil eden yapı
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}