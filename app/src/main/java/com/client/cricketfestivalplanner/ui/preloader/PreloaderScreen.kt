package com.client.cricketfestivalplanner.ui.preloader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.client.cricketfestivalplanner.theme.ColorTokens
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreloaderScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val viewModel: PreloaderViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (val s = state) {
            is PreloaderState.Success -> {
                if (s.onboardingCompleted) {
                    onNavigateToHome()
                } else {
                    onNavigateToOnboarding()
                }
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val s = state) {
            is PreloaderState.Loading -> {
                CircularProgressIndicator(
                    color = ColorTokens.PrimaryAccent,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorTokens.TextSecondary
                )
            }
            is PreloaderState.Error -> {
                Text(
                    text = "Initialization failed",
                    style = MaterialTheme.typography.titleLarge,
                    color = ColorTokens.Error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = s.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorTokens.TextSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { viewModel.initialize() }) {
                    Text("Retry")
                }
            }
            else -> Unit
        }
    }
}