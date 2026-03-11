package com.client.cricketfestivalplanner.ui.tournament

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.client.cricketfestivalplanner.data.db.MatchEntity
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import com.client.cricketfestivalplanner.utils.DateFormatter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailScreen(
    tournamentId: Int,
    onEdit: (Int) -> Unit,
    onSchedule: (Int) -> Unit,
    onTable: (Int) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: TournamentDetailViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(tournamentId) {
        viewModel.init(tournamentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournament Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(tournamentId) }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = ColorTokens.PrimaryAccent
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val s = state) {
            is TournamentDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }

            is TournamentDetailState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(Dimensions.md),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.md)
                ) {
                    item {
                        Text(
                            text = s.tournament.name,
                            style = MaterialTheme.typography.displayLarge,
                            color = ColorTokens.TextPrimary
                        )
                        Text(
                            text = "${s.tournament.matchType} • ${s.tournament.structure}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ColorTokens.TextSecondary
                        )
                        if (s.tournament.location.isNotEmpty()) {
                            Text(
                                text = s.tournament.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorTokens.TextSecondary
                            )
                        }
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
                            elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
                        ) {
                            Column(modifier = Modifier.padding(Dimensions.md)) {
                                Text(
                                    text = "Progress",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = ColorTokens.TextPrimary
                                )
                                Spacer(modifier = Modifier.height(Dimensions.sm))
                                Text(
                                    text = "${s.completedMatches} / ${s.totalMatches} matches completed",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = ColorTokens.TextSecondary
                                )
                                Spacer(modifier = Modifier.height(Dimensions.sm))
                                LinearProgressIndicator(
                                    progress = {
                                        if (s.totalMatches > 0)
                                            s.completedMatches.toFloat() / s.totalMatches
                                        else 0f
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    color = ColorTokens.PrimaryAccent
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Dimensions.sm)
                        ) {
                            OutlinedButton(
                                onClick = { onSchedule(tournamentId) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Schedule,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text("Schedule")
                            }
                            OutlinedButton(
                                onClick = { onTable(tournamentId) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.TableChart,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text("Table")
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Teams",
                            style = MaterialTheme.typography.titleLarge,
                            color = ColorTokens.TextPrimary
                        )
                    }

                    items(s.teams) { team ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
                            elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimensions.md),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = team.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = ColorTokens.TextPrimary
                                )
                                Text(
                                    text = "${team.points} pts",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = ColorTokens.PrimaryAccent
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Recent Matches",
                            style = MaterialTheme.typography.titleLarge,
                            color = ColorTokens.TextPrimary
                        )
                    }

                    items(s.matches.takeLast(3)) { match ->
                        MatchCard(match = match)
                    }

                    if (!s.tournament.isCompleted && s.completedMatches == s.totalMatches && s.totalMatches > 0) {
                        item {
                            Button(
                                onClick = { viewModel.completeTournament(s.tournament) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorTokens.Success
                                )
                            ) {
                                Text("Mark as Completed")
                            }
                        }
                    }
                }
            }

            is TournamentDetailState.Error -> {
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
private fun MatchCard(match: MatchEntity) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (match.isCompleted) ColorTokens.InputBackground else ColorTokens.Card
        ),
        elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${match.homeTeamName} vs ${match.awayTeamName}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorTokens.TextPrimary
                )
                Text(
                    text = DateFormatter.formatForDisplay(match.matchTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.TextSecondary
                )
            }
            if (match.isCompleted) {
                Text(
                    text = "${match.homeScore} - ${match.awayScore}",
                    style = MaterialTheme.typography.titleLarge,
                    color = ColorTokens.PrimaryAccent
                )
            } else {
                Text(
                    text = "Pending",
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.Warning
                )
            }
        }
    }
}