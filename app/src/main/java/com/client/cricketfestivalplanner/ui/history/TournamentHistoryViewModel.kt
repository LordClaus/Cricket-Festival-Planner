package com.client.cricketfestivalplanner.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class TournamentHistoryState {
    object Loading : TournamentHistoryState()
    data class Success(val tournaments: List<TournamentEntity>) : TournamentHistoryState()
    data class Error(val message: String) : TournamentHistoryState()
}

class TournamentHistoryViewModel(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TournamentHistoryState>(TournamentHistoryState.Loading)
    val state: StateFlow<TournamentHistoryState> = _state

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            tournamentRepository.getCompletedTournaments()
                .catch { e -> _state.value = TournamentHistoryState.Error(e.message ?: "Error") }
                .collect { tournaments ->
                    _state.value = TournamentHistoryState.Success(tournaments)
                }
        }
    }
}