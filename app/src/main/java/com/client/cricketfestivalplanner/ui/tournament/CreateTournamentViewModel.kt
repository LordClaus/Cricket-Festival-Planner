package com.client.cricketfestivalplanner.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.MatchEntity
import com.client.cricketfestivalplanner.data.db.TeamEntity
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import com.client.cricketfestivalplanner.utils.DateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreateTournamentState {
    object Idle : CreateTournamentState()
    object Loading : CreateTournamentState()
    data class Success(val tournamentId: Int) : CreateTournamentState()
    data class Error(val message: String) : CreateTournamentState()
}

class CreateTournamentViewModel(
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CreateTournamentState>(CreateTournamentState.Idle)
    val state: StateFlow<CreateTournamentState> = _state

    val matchTypes = listOf("T20", "ODI", "Test", "Custom")
    val pointSystems = listOf("Standard", "Net Run Rate", "Custom")
    val structures = listOf("Round Robin", "Knockout", "Group + Knockout")
    val durations = listOf("Single Day", "Weekend", "Week", "Month")

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _matchType = MutableStateFlow("T20")
    val matchType: StateFlow<String> = _matchType

    private val _teamCount = MutableStateFlow("4")
    val teamCount: StateFlow<String> = _teamCount

    private val _pointSystem = MutableStateFlow("Standard")
    val pointSystem: StateFlow<String> = _pointSystem

    private val _structure = MutableStateFlow("Round Robin")
    val structure: StateFlow<String> = _structure

    private val _duration = MutableStateFlow("Weekend")
    val duration: StateFlow<String> = _duration

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private val _teamNames = MutableStateFlow(listOf("Team 1", "Team 2", "Team 3", "Team 4"))
    val teamNames: StateFlow<List<String>> = _teamNames

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    private val _teamCountError = MutableStateFlow<String?>(null)
    val teamCountError: StateFlow<String?> = _teamCountError

    fun onNameChange(value: String) { _name.value = value; _nameError.value = null }
    fun onMatchTypeChange(value: String) { _matchType.value = value }
    fun onTeamCountChange(value: String) {
        _teamCount.value = value
        _teamCountError.value = null
        val count = value.toIntOrNull() ?: return
        if (count in 2..16) {
            val current = _teamNames.value.toMutableList()
            while (current.size < count) current.add("Team ${current.size + 1}")
            while (current.size > count) current.removeAt(current.lastIndex)
            _teamNames.value = current
        }
    }
    fun onPointSystemChange(value: String) { _pointSystem.value = value }
    fun onStructureChange(value: String) { _structure.value = value }
    fun onDurationChange(value: String) { _duration.value = value }
    fun onLocationChange(value: String) { _location.value = value }
    fun onTeamNameChange(index: Int, value: String) {
        val current = _teamNames.value.toMutableList()
        if (index < current.size) {
            current[index] = value
            _teamNames.value = current
        }
    }

    fun createTournament() {
        if (!validate()) return
        viewModelScope.launch {
            _state.value = CreateTournamentState.Loading
            try {
                val tournament = TournamentEntity(
                    name = _name.value.trim(),
                    matchType = _matchType.value,
                    teamCount = _teamCount.value.toInt(),
                    pointSystem = _pointSystem.value,
                    structure = _structure.value,
                    duration = _duration.value,
                    location = _location.value.trim()
                )
                val tournamentId = tournamentRepository.insertTournament(tournament).toInt()

                val teams = _teamNames.value.mapIndexed { index, teamName ->
                    TeamEntity(tournamentId = tournamentId, name = teamName.ifEmpty { "Team ${index + 1}" })
                }
                teamRepository.insertTeams(teams)

                val insertedTeams = teamRepository.getTeamsByTournamentIdOnce(tournamentId)
                val matches = generateSchedule(tournamentId, insertedTeams)
                matchRepository.insertMatches(matches)

                _state.value = CreateTournamentState.Success(tournamentId)
            } catch (e: Exception) {
                _state.value = CreateTournamentState.Error(e.message ?: "Failed to create tournament")
            }
        }
    }

    private fun generateSchedule(tournamentId: Int, teams: List<TeamEntity>): List<MatchEntity> {
        val matches = mutableListOf<MatchEntity>()
        var round = 1
        for (i in teams.indices) {
            for (j in i + 1 until teams.size) {
                matches.add(
                    MatchEntity(
                        tournamentId = tournamentId,
                        homeTeamId = teams[i].id,
                        awayTeamId = teams[j].id,
                        homeTeamName = teams[i].name,
                        awayTeamName = teams[j].name,
                        matchTime = DateFormatter.getCurrentDateForStorage(),
                        round = round++
                    )
                )
            }
        }
        return matches
    }

    private fun validate(): Boolean {
        var isValid = true
        if (_name.value.trim().isEmpty()) {
            _nameError.value = "Tournament name is required"
            isValid = false
        }
        val count = _teamCount.value.toIntOrNull()
        if (count == null || count < 2 || count > 16) {
            _teamCountError.value = "Team count must be between 2 and 16"
            isValid = false
        }
        return isValid
    }

    fun resetState() { _state.value = CreateTournamentState.Idle }
}