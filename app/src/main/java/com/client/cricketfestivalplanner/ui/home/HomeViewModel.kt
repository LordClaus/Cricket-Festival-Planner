package com.client.cricketfestivalplanner.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val activeTournaments: List<TournamentEntity>) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    init {
        loadActiveTournaments()
    }

    private fun loadActiveTournaments() {
        viewModelScope.launch {
            tournamentRepository.getActiveTournaments()
                .catch { e -> _state.value = HomeState.Error(e.message ?: "Unknown error") }
                .collect { tournaments -> _state.value = HomeState.Success(tournaments) }
        }
    }
}