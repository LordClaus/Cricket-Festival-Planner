package com.client.cricketfestivalplanner.ui.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen() {
    val viewModel: AnalyticsViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics") }
            )
        }
    ) { paddingValues ->
        when (val s = state) {
            is AnalyticsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }

            is AnalyticsState.Success -> {
                if (s.stats.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tournaments to analyze",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ColorTokens.TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(Dimensions.md),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.md)
                    ) {
                        items(s.stats) { stats ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
                                elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
                            ) {
                                Column(modifier = Modifier.padding(Dimensions.md)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stats.tournament.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = ColorTokens.TextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = if (stats.tournament.isCompleted) "Completed" else "Active",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (stats.tournament.isCompleted)
                                                ColorTokens.Success else ColorTokens.PrimaryAccent
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(Dimensions.sm))

                                    Text(
                                        text = "${stats.completedMatches} / ${stats.totalMatches} matches",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ColorTokens.TextSecondary
                                    )
                                    LinearProgressIndicator(
                                        progress = {
                                            if (stats.totalMatches > 0)
                                                stats.completedMatches.toFloat() / stats.totalMatches
                                            else 0f
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        color = ColorTokens.PrimaryAccent
                                    )

                                    Spacer(modifier = Modifier.height(Dimensions.sm))

                                    stats.topTeam?.let { team ->
                                        AnalyticsRow("Top Team", "${team.name} (${team.points} pts)")
                                    }
                                    AnalyticsRow("Teams", "${stats.teams.size}")
                                    stats.bestPlayer?.let { player ->
                                        AnalyticsRow("Best Player", player)
                                    }
                                    AnalyticsRow(
                                        "Format",
                                        "${stats.tournament.matchType} • ${stats.tournament.structure}"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AnalyticsState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = s.message,
                        color = ColorTokens.Error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalyticsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.xs),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = ColorTokens.TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = ColorTokens.TextPrimary
        )
    }
}