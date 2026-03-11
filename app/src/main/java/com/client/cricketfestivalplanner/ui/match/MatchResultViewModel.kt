package com.client.cricketfestivalplanner.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.MatchEntity
import com.client.cricketfestivalplanner.data.db.TeamEntity
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MatchResultState {
    object Loading : MatchResultState()
    data class Success(
        val match: MatchEntity,
        val homeTeam: TeamEntity?,
        val awayTeam: TeamEntity?
    ) : MatchResultState()
    data class Error(val message: String) : MatchResultState()
}

class MatchResultViewModel(
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MatchResultState>(MatchResultState.Loading)
    val state: StateFlow<MatchResultState> = _state

    private val _homeScore = MutableStateFlow("0")
    val homeScore: StateFlow<String> = _homeScore

    private val _awayScore = MutableStateFlow("0")
    val awayScore: StateFlow<String> = _awayScore

    private val _bestPlayerName = MutableStateFlow("")
    val bestPlayerName: StateFlow<String> = _bestPlayerName

    private val _homeScoreError = MutableStateFlow<String?>(null)
    val homeScoreError: StateFlow<String?> = _homeScoreError

    private val _awayScoreError = MutableStateFlow<String?>(null)
    val awayScoreError: StateFlow<String?> = _awayScoreError

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    fun init(matchId: Int) {
        viewModelScope.launch {
            _state.value = MatchResultState.Loading
            try {
                val match = matchRepository.getMatchById(matchId)
                if (match == null) {
                    _state.value = MatchResultState.Error("Match not found")
                    return@launch
                }
                if (match.isCompleted) {
                    _homeScore.value = match.homeScore.toString()
                    _awayScore.value = match.awayScore.toString()
                    _bestPlayerName.value = match.bestPlayerName ?: ""
                }
                val homeTeam = teamRepository.getTeamById(match.homeTeamId)
                val awayTeam = teamRepository.getTeamById(match.awayTeamId)
                _state.value = MatchResultState.Success(
                    match = match,
                    homeTeam = homeTeam,
                    awayTeam = awayTeam
                )
            } catch (e: Exception) {
                _state.value = MatchResultState.Error(e.message ?: "Error")
            }
        }
    }

    fun onHomeScoreChange(value: String) { _homeScore.value = value; _homeScoreError.value = null }
    fun onAwayScoreChange(value: String) { _awayScore.value = value; _awayScoreError.value = null }
    fun onBestPlayerNameChange(value: String) { _bestPlayerName.value = value }

    fun saveResult() {
        if (!validate()) return
        viewModelScope.launch {
            val s = _state.value as? MatchResultState.Success ?: return@launch
            val match = s.match
            val homeScore = _homeScore.value.toInt()
            val awayScore = _awayScore.value.toInt()

            matchRepository.updateMatch(
                match.copy(
                    homeScore = homeScore,
                    awayScore = awayScore,
                    bestPlayerName = _bestPlayerName.value.ifEmpty { null },
                    isCompleted = true
                )
            )

            val homeTeam = s.homeTeam
            val awayTeam = s.awayTeam

            if (homeTeam != null && awayTeam != null) {
                val homeWon = homeScore > awayScore
                val awayWon = awayScore > homeScore

                teamRepository.updateTeam(
                    homeTeam.copy(
                        matchesPlayed = homeTeam.matchesPlayed + 1,
                        matchesWon = homeTeam.matchesWon + if (homeWon) 1 else 0,
                        matchesLost = homeTeam.matchesLost + if (awayWon) 1 else 0,
                        points = homeTeam.points + if (homeWon) 2 else if (!homeWon && !awayWon) 1 else 0,
                        runDifference = homeTeam.runDifference + (homeScore - awayScore)
                    )
                )
                teamRepository.updateTeam(
                    awayTeam.copy(
                        matchesPlayed = awayTeam.matchesPlayed + 1,
                        matchesWon = awayTeam.matchesWon + if (awayWon) 1 else 0,
                        matchesLost = awayTeam.matchesLost + if (homeWon) 1 else 0,
                        points = awayTeam.points + if (awayWon) 2 else if (!homeWon && !awayWon) 1 else 0,
                        runDifference = awayTeam.runDifference + (awayScore - homeScore)
                    )
                )
            }
            _saveSuccess.value = true
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (_homeScore.value.toIntOrNull() == null || _homeScore.value.toInt() < 0) {
            _homeScoreError.value = "Invalid score"
            isValid = false
        }
        if (_awayScore.value.toIntOrNull() == null || _awayScore.value.toInt() < 0) {
            _awayScoreError.value = "Invalid score"
            isValid = false
        }
        return isValid
    }

    fun resetSaveSuccess() { _saveSuccess.value = false }
}