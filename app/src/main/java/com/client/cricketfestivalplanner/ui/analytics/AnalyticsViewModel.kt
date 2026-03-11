package com.client.cricketfestivalplanner.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.TeamEntity
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class TournamentStats(
    val tournament: TournamentEntity,
    val teams: List<TeamEntity>,
    val totalMatches: Int,
    val completedMatches: Int,
    val topTeam: TeamEntity?,
    val bestPlayer: String?
)

sealed class AnalyticsState {
    object Loading : AnalyticsState()
    data class Success(val stats: List<TournamentStats>) : AnalyticsState()
    data class Error(val message: String) : AnalyticsState()
}

class AnalyticsViewModel(
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AnalyticsState>(AnalyticsState.Loading)
    val state: StateFlow<AnalyticsState> = _state

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            tournamentRepository.getAllTournaments()
                .catch { e -> _state.value = AnalyticsState.Error(e.message ?: "Error") }
                .collect { tournaments ->
                    val statsList = tournaments.map { tournament ->
                        val teams = teamRepository.getTeamsByTournamentIdOnce(tournament.id)
                        val matches = matchRepository.getMatchesByTournamentIdOnce(tournament.id)
                        val completedMatches = matches.count { it.isCompleted }
                        val topTeam = teams.maxByOrNull { it.points }
                        val bestPlayer = matches
                            .filter { it.isCompleted && it.bestPlayerName != null }
                            .groupBy { it.bestPlayerName }
                            .maxByOrNull { it.value.size }
                            ?.key
                        TournamentStats(
                            tournament = tournament,
                            teams = teams,
                            totalMatches = matches.size,
                            completedMatches = completedMatches,
                            topTeam = topTeam,
                            bestPlayer = bestPlayer
                        )
                    }
                    _state.value = AnalyticsState.Success(statsList)
                }
        }
    }
}