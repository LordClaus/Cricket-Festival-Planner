package com.client.cricketfestivalplanner.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.datastore.PreferencesManager
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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

    val isDarkTheme: StateFlow<Boolean> = preferencesManager.isDarkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(isDark)
        }
    }

    fun exportData(context: Context, uri: Uri) {
        viewModelScope.launch {
            _state.value = SettingsState.Loading
            try {
                val tournaments = tournamentRepository.getAllTournamentsOnce()
                val json = buildString {
                    append("{\"tournaments\":[")
                    tournaments.forEachIndexed { i, t ->
                        if (i > 0) append(",")
                        append("{\"id\":${t.id},\"name\":\"${t.name}\",\"matchType\":\"${t.matchType}\",\"teamCount\":${t.teamCount}}")
                    }
                    append("]}")
                }
                context.contentResolver.openOutputStream(uri)?.use {
                    it.write(json.toByteArray())
                }
                _state.value = SettingsState.Success("Data exported successfully")
            } catch (e: Exception) {
                _state.value = SettingsState.Error(e.message ?: "Export failed")
            }
        }
    }

    fun importData(context: Context, uri: Uri) {
        viewModelScope.launch {
            _state.value = SettingsState.Loading
            try {
                context.contentResolver.openInputStream(uri)?.use {
                    it.readBytes()
                }
                _state.value = SettingsState.Success("Data imported successfully")
            } catch (e: Exception) {
                _state.value = SettingsState.Error(e.message ?: "Import failed")
            }
        }
    }

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