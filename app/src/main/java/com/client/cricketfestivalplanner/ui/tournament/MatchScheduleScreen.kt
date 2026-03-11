package com.client.cricketfestivalplanner.ui.tournament

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.client.cricketfestivalplanner.data.db.MatchEntity
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import com.client.cricketfestivalplanner.utils.DateFormatter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScheduleScreen(
    tournamentId: Int,
    onMatchClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: MatchScheduleViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    var editingMatch by remember { mutableStateOf<MatchEntity?>(null) }
    var editTime by remember { mutableStateOf("") }

    LaunchedEffect(tournamentId) {
        viewModel.init(tournamentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match Schedule") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val s = state) {
            is MatchScheduleState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }

            is MatchScheduleState.Success -> {
                if (s.matches.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matches scheduled",
                            style = MaterialTheme.typography.bodyLarge,
                            color = ColorTokens.TextSecondary
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
                        items(s.matches) { match ->
                            ScheduleMatchCard(
                                match = match,
                                onEditTime = {
                                    editingMatch = match
                                    editTime = match.matchTime
                                },
                                onEnterResult = {
                                    onMatchClick(match.id)
                                }
                            )
                        }
                    }
                }
            }

            is MatchScheduleState.Error -> {
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

        editingMatch?.let { match ->
            AlertDialog(
                onDismissRequest = { editingMatch = null },
                title = { Text("Edit Match Time") },
                text = {
                    OutlinedTextField(
                        value = editTime,
                        onValueChange = { editTime = it },
                        label = { Text("Date (yyyy-MM-dd)") }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.updateMatchTime(match, editTime)
                            editingMatch = null
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingMatch = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun ScheduleMatchCard(
    match: MatchEntity,
    onEditTime: () -> Unit,
    onEnterResult: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (match.isCompleted) ColorTokens.InputBackground else ColorTokens.Card
        ),
        elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
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
                    text = "Round ${match.round}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.TextSecondary
                )
                if (match.isCompleted) {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Success
                    )
                }
            }
            Text(
                text = "${match.homeTeamName} vs ${match.awayTeamName}",
                style = MaterialTheme.typography.titleLarge,
                color = ColorTokens.TextPrimary
            )
            if (match.isCompleted) {
                Text(
                    text = "Score: ${match.homeScore} - ${match.awayScore}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorTokens.PrimaryAccent
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = DateFormatter.formatForDisplay(match.matchTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.TextSecondary
                )
                Row {
                    IconButton(onClick = onEditTime) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Time",
                            tint = ColorTokens.PrimaryAccent
                        )
                    }
                    if (!match.isCompleted) {
                        TextButton(onClick = onEnterResult) {
                            Text("Enter Result", color = ColorTokens.PrimaryAccent)
                        }
                    }
                }
            }
        }
    }
}