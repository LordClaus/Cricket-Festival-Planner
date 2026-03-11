package com.client.cricketfestivalplanner.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.TeamEntity
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class TournamentTableState {
    object Loading : TournamentTableState()
    data class Success(
        val tournament: TournamentEntity,
        val teams: List<TeamEntity>
    ) : TournamentTableState()
    data class Error(val message: String) : TournamentTableState()
}

class TournamentTableViewModel(
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TournamentTableState>(TournamentTableState.Loading)
    val state: StateFlow<TournamentTableState> = _state

    fun init(tournamentId: Int) {
        viewModelScope.launch {
            val tournament = tournamentRepository.getTournamentById(tournamentId)
            if (tournament == null) {
                _state.value = TournamentTableState.Error("Tournament not found")
                return@launch
            }
            teamRepository.getTeamsByTournamentId(tournamentId)
                .catch { e -> _state.value = TournamentTableState.Error(e.message ?: "Error") }
                .collect { teams ->
                    _state.value = TournamentTableState.Success(
                        tournament = tournament,
                        teams = teams
                    )
                }
        }
    }
}