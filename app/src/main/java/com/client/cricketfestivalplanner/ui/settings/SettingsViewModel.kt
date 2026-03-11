package com.client.cricketfestivalplanner.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.datastore.PreferencesManager
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SettingsState {
    object Idle : SettingsState()
    object Loading : SettingsState()
    data class Success(val message: String) : SettingsState()
    data class Error(val message: String) : SettingsState()
}

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(SettingsState.Idle)
    val state: StateFlow<SettingsState> = _state

    fun clearAllData() {
        viewModelScope.launch {
            _state.value = SettingsState.Loading
            try {
                matchRepository.deleteAllMatches()
                tournamentRepository.deleteAllTournaments()
                _state.value = SettingsState.Success("All data cleared successfully")
            } catch (e: Exception) {
                _state.value = SettingsState.Error(e.message ?: "Failed to clear data")
            }
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            _state.value = SettingsState.Loading
            try {
                preferencesManager.resetAllPreferences()
                _state.value = SettingsState.Success("Settings reset successfully")
            } catch (e: Exception) {
                _state.value = SettingsState.Error(e.message ?: "Failed to reset settings")
            }
        }
    }

    fun resetState() {
        _state.value = SettingsState.Idle
    }
}