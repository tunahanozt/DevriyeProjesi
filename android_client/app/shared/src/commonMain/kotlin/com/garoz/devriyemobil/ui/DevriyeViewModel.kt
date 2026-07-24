package com.garoz.devriyemobil.ui

import com.garoz.devriyemobil.network.PatrolDto
import com.garoz.devriyemobil.network.PatrolRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DevriyeViewModel(private val repository: PatrolRepository) {

    private val _uiState = MutableStateFlow<DevriyeState>(DevriyeState.Loading)
    val uiState: StateFlow<DevriyeState> = _uiState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default)

    fun yukle() {
        _uiState.value = DevriyeState.Loading
        scope.launch {
            repository.getMyPatrols().fold(
                onSuccess = { _uiState.value = DevriyeState.Success(it) },
                onFailure = { _uiState.value = DevriyeState.Error(it.message ?: "Bilinmeyen hata") }
            )
        }
    }

    fun baslat(id: Int) {
        scope.launch {
            repository.startPatrol(id).onSuccess { yukle() }
        }
    }

    fun tamamla(id: Int) {
        scope.launch {
            repository.completePatrol(id).onSuccess { yukle() }
        }
    }
}

sealed class DevriyeState {
    object Loading : DevriyeState()
    data class Success(val patrols: List<PatrolDto>) : DevriyeState()
    data class Error(val message: String) : DevriyeState()
}
