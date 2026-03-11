package com.client.cricketfestivalplanner.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.MatchEntity
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class MatchScheduleState {
    object Loading : MatchScheduleState()
    data class Success(
        val tournament: TournamentEntity,
        val matches: List<MatchEntity>
    ) : MatchScheduleState()
    data class Error(val message: String) : MatchScheduleState()
}

class MatchScheduleViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MatchScheduleState>(MatchScheduleState.Loading)
    val state: StateFlow<MatchScheduleState> = _state

    fun init(tournamentId: Int) {
        viewModelScope.launch {
            val tournament = tournamentRepository.getTournamentById(tournamentId)
            if (tournament == null) {
                _state.value = MatchScheduleState.Error("Tournament not found")
                return@launch
            }
            matchRepository.getMatchesByTournamentId(tournamentId)
                .catch { e -> _state.value = MatchScheduleState.Error(e.message ?: "Error") }
                .collect { matches ->
                    _state.value = MatchScheduleState.Success(
                        tournament = tournament,
                        matches = matches
                    )
                }
        }
    }

    fun updateMatchTime(match: MatchEntity, newTime: String) {
        viewModelScope.launch {
            matchRepository.updateMatch(match.copy(matchTime = newTime))
        }
    }
}