package com.client.cricketfestivalplanner.ui.preloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.datastore.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class PreloaderState {
    object Loading : PreloaderState()
    data class Success(val onboardingCompleted: Boolean) : PreloaderState()
    data class Error(val message: String) : PreloaderState()
}

class PreloaderViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow<PreloaderState>(PreloaderState.Loading)
    val state: StateFlow<PreloaderState> = _state

    init {
        initialize()
    }

    fun initialize() {
        viewModelScope.launch {
            _state.value = PreloaderState.Loading
            try {
                val onboardingCompleted = preferencesManager.isOnboardingCompleted.first()
                _state.value = PreloaderState.Success(onboardingCompleted)
            } catch (e: Exception) {
                _state.value = PreloaderState.Error(e.message ?: "Initialization failed")
            }
        }
    }
}