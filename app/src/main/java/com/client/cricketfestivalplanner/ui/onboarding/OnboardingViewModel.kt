package com.client.cricketfestivalplanner.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.client.cricketfestivalplanner.data.datastore.PreferencesManager
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            preferencesManager.setOnboardingCompleted()
            onComplete()
        }
    }
}