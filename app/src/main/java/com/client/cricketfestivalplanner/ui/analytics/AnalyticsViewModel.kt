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

data class TeamStats(
    val team: TeamEntity,
    val winRate: Float
)

data class PlayerStat(
    val name: String,
    val appearances: Int
)

data class TournamentStats(
    val tournament: TournamentEntity,
    val teams: List<TeamEntity>,
    val totalMatches: Int,
    val completedMatches: Int,
    val topTeam: TeamEntity?,
    val bestPlayers: List<PlayerStat>,
    val teamStats: List<TeamStats>,
    val totalRuns: Int,
    val avgRunsPerMatch: Float
)

sealed class AnalyticsState {
    object Loading : AnalyticsState()
    data class Success(
        val stats: List<TournamentStats>,
        val totalTournaments: Int,
        val completedTournaments: Int,
        val globalBestPlayer: String?,
        val globalTopTeam: String?
    ) : AnalyticsState()
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
                        val completedMatches = matches.filter { it.isCompleted }

                        val topTeam = teams.maxByOrNull { it.points }

                        val bestPlayers = matches
                            .filter { it.isCompleted && it.bestPlayerName != null }
                            .groupBy { it.bestPlayerName!! }
                            .map { PlayerStat(it.key, it.value.size) }
                            .sortedByDescending { it.appearances }
                            .take(3)

                        val teamStats = teams.map { team ->
                            val winRate = if (team.matchesPlayed > 0)
                                team.matchesWon.toFloat() / team.matchesPlayed
                            else 0f
                            TeamStats(team, winRate)
                        }.sortedByDescending { it.team.points }

                        val totalRuns = completedMatches.sumOf { it.homeScore + it.awayScore }
                        val avgRuns = if (completedMatches.isNotEmpty())
                            totalRuns.toFloat() / completedMatches.size else 0f

                        TournamentStats(
                            tournament = tournament,
                            teams = teams,
                            totalMatches = matches.size,
                            completedMatches = completedMatches.size,
                            topTeam = topTeam,
                            bestPlayers = bestPlayers,
                            teamStats = teamStats,
                            totalRuns = totalRuns,
                            avgRunsPerMatch = avgRuns
                        )
                    }

                    val globalBestPlayer = statsList
                        .flatMap { it.bestPlayers }
                        .groupBy { it.name }
                        .maxByOrNull { it.value.sumOf { p -> p.appearances } }
                        ?.key

                    val globalTopTeam = statsList
                        .mapNotNull { it.topTeam }
                        .maxByOrNull { it.points }
                        ?.name

                    _state.value = AnalyticsState.Success(
                        stats = statsList,
                        totalTournaments = tournaments.size,
                        completedTournaments = tournaments.count { it.isCompleted },
                        globalBestPlayer = globalBestPlayer,
                        globalTopTeam = globalTopTeam
                    )
                }
        }
    }
}