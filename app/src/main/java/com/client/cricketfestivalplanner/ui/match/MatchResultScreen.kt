package com.client.cricketfestivalplanner.ui.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchResultScreen(
    matchId: Int,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: MatchResultViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val homeScore by viewModel.homeScore.collectAsState()
    val awayScore by viewModel.awayScore.collectAsState()
    val bestPlayerName by viewModel.bestPlayerName.collectAsState()
    val homeScoreError by viewModel.homeScoreError.collectAsState()
    val awayScoreError by viewModel.awayScoreError.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.init(matchId)
    }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onSaved()
            viewModel.resetSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match Result") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val s = state) {
            is MatchResultState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }

            is MatchResultState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(Dimensions.md),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.md)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
                        elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimensions.md),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Round ${s.match.round}",
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorTokens.TextSecondary
                            )
                            Spacer(modifier = Modifier.height(Dimensions.sm))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = s.match.homeTeamName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = ColorTokens.TextPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "vs",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = ColorTokens.TextSecondary
                                )
                                Text(
                                    text = s.match.awayTeamName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = ColorTokens.TextPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.md)
                    ) {
                        OutlinedTextField(
                            value = homeScore,
                            onValueChange = viewModel::onHomeScoreChange,
                            label = { Text(s.match.homeTeamName) },
                            isError = homeScoreError != null,
                            supportingText = { homeScoreError?.let { Text(it) } },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = awayScore,
                            onValueChange = viewModel::onAwayScoreChange,
                            label = { Text(s.match.awayTeamName) },
                            isError = awayScoreError != null,
                            supportingText = { awayScoreError?.let { Text(it) } },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = bestPlayerName,
                        onValueChange = viewModel::onBestPlayerNameChange,
                        label = { Text("Best Player (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.sm))

                    Button(
                        onClick = { viewModel.saveResult() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Result")
                    }
                }
            }

            is MatchResultState.Error -> {
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