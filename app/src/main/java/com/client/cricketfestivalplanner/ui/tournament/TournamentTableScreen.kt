package com.client.cricketfestivalplanner.ui.tournament

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentTableScreen(
    tournamentId: Int,
    onBack: () -> Unit
) {
    val viewModel: TournamentTableViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(tournamentId) {
        viewModel.init(tournamentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournament Table") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val s = state) {
            is TournamentTableState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }

            is TournamentTableState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(Dimensions.md)
                ) {
                    Text(
                        text = s.tournament.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = ColorTokens.TextPrimary
                    )
                    Text(
                        text = "Standings",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ColorTokens.TextSecondary
                    )

                    androidx.compose.foundation.layout.Spacer(
                        modifier = Modifier.padding(top = Dimensions.md)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
                        elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ColorTokens.PrimaryAccent)
                                    .padding(Dimensions.sm),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TableHeader(text = "#", modifier = Modifier.weight(0.5f))
                                TableHeader(text = "Team", modifier = Modifier.weight(2f))
                                TableHeader(text = "P", modifier = Modifier.weight(0.7f))
                                TableHeader(text = "W", modifier = Modifier.weight(0.7f))
                                TableHeader(text = "L", modifier = Modifier.weight(0.7f))
                                TableHeader(text = "RD", modifier = Modifier.weight(0.7f))
                                TableHeader(text = "Pts", modifier = Modifier.weight(0.7f))
                            }

                            LazyColumn {
                                itemsIndexed(s.teams) { index, team ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (index % 2 == 0) ColorTokens.Card
                                                else ColorTokens.InputBackground
                                            )
                                            .padding(Dimensions.sm),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TableCell(
                                            text = "${index + 1}",
                                            modifier = Modifier.weight(0.5f),
                                            color = if (index == 0) ColorTokens.PrimaryAccent
                                            else ColorTokens.TextSecondary
                                        )
                                        TableCell(
                                            text = team.name,
                                            modifier = Modifier.weight(2f),
                                            fontWeight = if (index == 0) FontWeight.Bold
                                            else FontWeight.Normal
                                        )
                                        TableCell(text = "${team.matchesPlayed}", modifier = Modifier.weight(0.7f))
                                        TableCell(text = "${team.matchesWon}", modifier = Modifier.weight(0.7f))
                                        TableCell(text = "${team.matchesLost}", modifier = Modifier.weight(0.7f))
                                        TableCell(text = "${team.runDifference}", modifier = Modifier.weight(0.7f))
                                        TableCell(
                                            text = "${team.points}",
                                            modifier = Modifier.weight(0.7f),
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.PrimaryAccent
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is TournamentTableState.Error -> {
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
private fun TableHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = ColorTokens.Card,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun TableCell(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    color: androidx.compose.ui.graphics.Color = ColorTokens.TextPrimary
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}