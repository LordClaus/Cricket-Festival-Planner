package com.client.cricketfestivalplanner.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.client.cricketfestivalplanner.utils.DateFormatter
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentHistoryScreen(
    onTournamentClick: (Int) -> Unit
) {
    val viewModel: TournamentHistoryViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") }
            )
        }
    ) { paddingValues ->
        when (val s = state) {
            is TournamentHistoryState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }

            is TournamentHistoryState.Success -> {
                if (s.tournaments.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No completed tournaments yet",
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
                        verticalArrangement = Arrangement.spacedBy(Dimensions.sm)
                    ) {
                        items(s.tournaments) { tournament ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
                                elevation = CardDefaults.cardElevation(Dimensions.cardElevation),
                                onClick = { onTournamentClick(tournament.id) }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(Dimensions.md)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = tournament.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = ColorTokens.TextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = tournament.matchType,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ColorTokens.PrimaryAccent
                                        )
                                    }
                                    Text(
                                        text = "${tournament.teamCount} teams • ${tournament.structure}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = ColorTokens.TextSecondary
                                    )
                                    Text(
                                        text = DateFormatter.formatForDisplay(
                                            DateFormatter.formatForStorage(Date(tournament.createdAt))
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ColorTokens.TextSecondary
                                    )
                                    Text(
                                        text = "Completed",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ColorTokens.Success
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is TournamentHistoryState.Error -> {
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