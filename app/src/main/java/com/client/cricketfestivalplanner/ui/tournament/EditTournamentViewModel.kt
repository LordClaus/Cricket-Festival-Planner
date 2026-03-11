package com.client.cricketfestivalplanner.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class EditTournamentState {
    object Loading : EditTournamentState()
    object Idle : EditTournamentState()
    object Success : EditTournamentState()
    data class Error(val message: String) : EditTournamentState()
}

class EditTournamentViewModel(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EditTournamentState>(EditTournamentState.Loading)
    val state: StateFlow<EditTournamentState> = _state

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _matchType = MutableStateFlow("T20")
    val matchType: StateFlow<String> = _matchType

    private val _pointSystem = MutableStateFlow("Standard")
    val pointSystem: StateFlow<String> = _pointSystem

    private val _structure = MutableStateFlow("Round Robin")
    val structure: StateFlow<String> = _structure

    private val _duration = MutableStateFlow("Weekend")
    val duration: StateFlow<String> = _duration

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    val matchTypes = listOf("T20", "ODI", "Test", "Custom")
    val pointSystems = listOf("Standard", "Net Run Rate", "Custom")
    val structures = listOf("Round Robin", "Knockout", "Group + Knockout")
    val durations = listOf("Single Day", "Weekend", "Week", "Month")

    private var originalTournament: TournamentEntity? = null

    fun init(tournamentId: Int) {
        viewModelScope.launch {
            val tournament = tournamentRepository.getTournamentById(tournamentId)
            if (tournament == null) {
                _state.value = EditTournamentState.Error("Tournament not found")
                return@launch
            }
            originalTournament = tournament
            _name.value = tournament.name
            _matchType.value = tournament.matchType
            _pointSystem.value = tournament.pointSystem
            _structure.value = tournament.structure
            _duration.value = tournament.duration
            _location.value = tournament.location
            _state.value = EditTournamentState.Idle
        }
    }

    fun onNameChange(value: String) { _name.value = value; _nameError.value = null }
    fun onMatchTypeChange(value: String) { _matchType.value = value }
    fun onPointSystemChange(value: String) { _pointSystem.value = value }
    fun onStructureChange(value: String) { _structure.value = value }
    fun onDurationChange(value: String) { _duration.value = value }
    fun onLocationChange(value: String) { _location.value = value }

    fun saveChanges() {
        if (_name.value.trim().isEmpty()) {
            _nameError.value = "Tournament name is required"
            return
        }
        viewModelScope.launch {
            _state.value = EditTournamentState.Loading
            try {
                val original = originalTournament ?: return@launch
                tournamentRepository.updateTournament(
                    original.copy(
                        name = _name.value.trim(),
                        matchType = _matchType.value,
                        pointSystem = _pointSystem.value,
                        structure = _structure.value,
                        duration = _duration.value,
                        location = _location.value.trim()
                    )
                )
                _state.value = EditTournamentState.Success
            } catch (e: Exception) {
                _state.value = EditTournamentState.Error(e.message ?: "Failed to save")
            }
        }
    }

    fun resetState() { _state.value = EditTournamentState.Idle }
}