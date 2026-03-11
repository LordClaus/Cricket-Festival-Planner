package com.client.cricketfestivalplanner.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.MatchEntity
import com.client.cricketfestivalplanner.data.db.TeamEntity
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class TournamentDetailState {
    object Loading : TournamentDetailState()
    data class Success(
        val tournament: TournamentEntity,
        val teams: List<TeamEntity>,
        val matches: List<MatchEntity>,
        val completedMatches: Int,
        val totalMatches: Int
    ) : TournamentDetailState()
    data class Error(val message: String) : TournamentDetailState()
}

class TournamentDetailViewModel(
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TournamentDetailState>(TournamentDetailState.Loading)
    val state: StateFlow<TournamentDetailState> = _state

    fun init(tournamentId: Int) {
        viewModelScope.launch {
            val tournament = tournamentRepository.getTournamentById(tournamentId)
            if (tournament == null) {
                _state.value = TournamentDetailState.Error("Tournament not found")
                return@launch
            }
            matchRepository.getMatchesByTournamentId(tournamentId)
                .catch { e -> _state.value = TournamentDetailState.Error(e.message ?: "Error") }
                .collect { matches ->
                    val teams = teamRepository.getTeamsByTournamentIdOnce(tournamentId)
                    val completedMatches = matches.count { it.isCompleted }
                    _state.value = TournamentDetailState.Success(
                        tournament = tournament,
                        teams = teams,
                        matches = matches,
                        completedMatches = completedMatches,
                        totalMatches = matches.size
                    )
                }
        }
    }

    fun completeTournament(tournament: TournamentEntity) {
        viewModelScope.launch {
            tournamentRepository.updateTournament(tournament.copy(isCompleted = true))
        }
    }
}