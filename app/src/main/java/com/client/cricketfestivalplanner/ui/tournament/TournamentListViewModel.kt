package com.client.cricketfestivalplanner.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class TournamentListState {
    object Loading : TournamentListState()
    data class Success(val tournaments: List<TournamentEntity>) : TournamentListState()
    data class Error(val message: String) : TournamentListState()
}

class TournamentListViewModel(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TournamentListState>(TournamentListState.Loading)
    val state: StateFlow<TournamentListState> = _state

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var allTournaments: List<TournamentEntity> = emptyList()

    init {
        loadTournaments()
    }

    private fun loadTournaments() {
        viewModelScope.launch {
            tournamentRepository.getAllTournaments()
                .catch { e -> _state.value = TournamentListState.Error(e.message ?: "Error") }
                .collect { tournaments ->
                    allTournaments = tournaments
                    applyFilter()
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFilter()
    }

    private fun applyFilter() {
        val query = _searchQuery.value.lowercase()
        val filtered = if (query.isEmpty()) allTournaments
        else allTournaments.filter { it.name.lowercase().contains(query) }
        _state.value = TournamentListState.Success(filtered)
    }

    fun deleteTournament(tournament: TournamentEntity) {
        viewModelScope.launch {
            tournamentRepository.deleteTournament(tournament)
        }
    }
}