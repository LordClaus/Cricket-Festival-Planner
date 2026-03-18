package com.client.cricketfestivalplanner.ui.analytics

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
            TopAppBar(title = { Text("Analytics") })
        }
    ) { paddingValues ->
        when (val s = state) {
            is AnalyticsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }

            is AnalyticsState.Success -> {
                if (s.stats.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
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
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentPadding = PaddingValues(Dimensions.md),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.md)
                    ) {
                        item {
                            GlobalStatsCard(s)
                        }

                        items(s.stats) { stats ->
                            TournamentStatsCard(stats)
                        }
                    }
                }
            }

            is AnalyticsState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
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
private fun GlobalStatsCard(s: AnalyticsState.Success) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ColorTokens.PrimaryAccent),
        elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
    ) {
        Column(modifier = Modifier.padding(Dimensions.md)) {
            Text(
                text = "Overall Statistics",
                style = MaterialTheme.typography.titleLarge,
                color = ColorTokens.Card,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(Dimensions.sm))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GlobalStatItem("Tournaments", "${s.totalTournaments}")
                GlobalStatItem("Completed", "${s.completedTournaments}")
                GlobalStatItem("Active", "${s.totalTournaments - s.completedTournaments}")
            }
            Spacer(modifier = Modifier.height(Dimensions.sm))
            s.globalTopTeam?.let {
                Text(
                    text = "🏆 Top Team: $it",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorTokens.Card
                )
            }
            s.globalBestPlayer?.let {
                Text(
                    text = "⭐ Best Player: $it",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorTokens.Card
                )
            }
        }
    }
}

@Composable
private fun GlobalStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = ColorTokens.Card,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = ColorTokens.Card.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun TournamentStatsCard(stats: TournamentStats) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
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
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (stats.tournament.isCompleted) "Completed" else "Active",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (stats.tournament.isCompleted)
                        ColorTokens.Success else ColorTokens.PrimaryAccent
                )
            }

            Text(
                text = "${stats.tournament.matchType} • ${stats.tournament.structure}",
                style = MaterialTheme.typography.bodySmall,
                color = ColorTokens.TextSecondary
            )

            Spacer(modifier = Modifier.height(Dimensions.sm))

            // Progress
            Text(
                text = "Match Progress: ${stats.completedMatches}/${stats.totalMatches}",
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

            // Run stats
            if (stats.completedMatches > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatChip("Total Runs", "${stats.totalRuns}")
                    StatChip("Avg/Match", "${"%.1f".format(stats.avgRunsPerMatch)}")
                    StatChip("Teams", "${stats.teams.size}")
                }
                Spacer(modifier = Modifier.height(Dimensions.sm))
            }

            // Team standings
            if (stats.teamStats.isNotEmpty()) {
                Text(
                    text = "Team Rankings",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Table header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorTokens.PrimaryAccent)
                        .padding(horizontal = Dimensions.sm, vertical = 4.dp)
                ) {
                    Text("#", style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Card, modifier = Modifier.width(24.dp))
                    Text("Team", style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Card, modifier = Modifier.weight(1f))
                    Text("W", style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Card, modifier = Modifier.width(28.dp),
                        textAlign = TextAlign.Center)
                    Text("L", style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Card, modifier = Modifier.width(28.dp),
                        textAlign = TextAlign.Center)
                    Text("Pts", style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Card, modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center)
                    Text("WR%", style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Card, modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center)
                }

                stats.teamStats.forEachIndexed { index, ts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (index % 2 == 0) MaterialTheme.colorScheme.surface
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = Dimensions.sm, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (index == 0) ColorTokens.PrimaryAccent
                            else ColorTokens.TextSecondary,
                            fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.width(24.dp)
                        )
                        Text(
                            text = ts.team.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${ts.team.matchesWon}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.Success,
                            modifier = Modifier.width(28.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${ts.team.matchesLost}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.Error,
                            modifier = Modifier.width(28.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${ts.team.points}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.PrimaryAccent,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(36.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${"%.0f".format(ts.winRate * 100)}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.TextSecondary,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Best players
            if (stats.bestPlayers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimensions.sm))
                Text(
                    text = "Top Players",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                stats.bestPlayers.forEachIndexed { index, player ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = when (index) { 0 -> "🥇"; 1 -> "🥈"; else -> "🥉" },
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = player.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${player.appearances}x MVP",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.PrimaryAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = Dimensions.sm, vertical = 4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = ColorTokens.PrimaryAccent,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = ColorTokens.TextSecondary
        )
    }
}