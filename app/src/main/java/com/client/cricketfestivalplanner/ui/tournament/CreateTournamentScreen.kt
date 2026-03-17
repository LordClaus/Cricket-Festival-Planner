package com.client.cricketfestivalplanner.ui.tournament

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTournamentScreen(
    onTournamentCreated: (Int) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: CreateTournamentViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val name by viewModel.name.collectAsState()
    val matchType by viewModel.matchType.collectAsState()
    val teamCount by viewModel.teamCount.collectAsState()
    val pointSystem by viewModel.pointSystem.collectAsState()
    val structure by viewModel.structure.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val location by viewModel.location.collectAsState()
    val teamNames by viewModel.teamNames.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val teamCountError by viewModel.teamCountError.collectAsState()

    var matchTypeExpanded by remember { mutableStateOf(false) }
    var pointSystemExpanded by remember { mutableStateOf(false) }
    var structureExpanded by remember { mutableStateOf(false) }
    var durationExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is CreateTournamentState.Success) {
            onTournamentCreated((state as CreateTournamentState.Success).tournamentId)
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Tournament") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimensions.md)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimensions.md)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { if (it.length <= 30) viewModel.onNameChange(it) },
                label = { Text("Tournament Name") },
                singleLine = true,
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = matchTypeExpanded,
                onExpandedChange = { matchTypeExpanded = it }
            ) {
                OutlinedTextField(
                    value = matchType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Match Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = matchTypeExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = matchTypeExpanded,
                    onDismissRequest = { matchTypeExpanded = false }
                ) {
                    viewModel.matchTypes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { viewModel.onMatchTypeChange(option); matchTypeExpanded = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = teamCount,
                onValueChange = viewModel::onTeamCountChange,
                label = { Text("Number of Teams (2-16)") },
                isError = teamCountError != null,
                supportingText = { teamCountError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = pointSystemExpanded,
                onExpandedChange = { pointSystemExpanded = it }
            ) {
                OutlinedTextField(
                    value = pointSystem,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Point System") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pointSystemExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = pointSystemExpanded,
                    onDismissRequest = { pointSystemExpanded = false }
                ) {
                    viewModel.pointSystems.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { viewModel.onPointSystemChange(option); pointSystemExpanded = false }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = structureExpanded,
                onExpandedChange = { structureExpanded = it }
            ) {
                OutlinedTextField(
                    value = structure,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Structure") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = structureExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = structureExpanded,
                    onDismissRequest = { structureExpanded = false }
                ) {
                    viewModel.structures.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { viewModel.onStructureChange(option); structureExpanded = false }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = durationExpanded,
                onExpandedChange = { durationExpanded = it }
            ) {
                OutlinedTextField(
                    value = duration,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Duration") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = durationExpanded,
                    onDismissRequest = { durationExpanded = false }
                ) {
                    viewModel.durations.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { viewModel.onDurationChange(option); durationExpanded = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = location,
                onValueChange = { if (it.length <= 50) viewModel.onLocationChange(it) },
                label = { Text("Location (optional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()

            Text(
                text = "Team Names",
                style = MaterialTheme.typography.titleLarge,
                color = ColorTokens.TextPrimary
            )

            teamNames.forEachIndexed { index, teamName ->
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { if (it.length <= 20) viewModel.onTeamNameChange(index, it) },
                    label = { Text("Team ${index + 1}") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(Dimensions.sm))

            when (state) {
                is CreateTournamentState.Loading -> {
                    CircularProgressIndicator(
                        color = ColorTokens.PrimaryAccent,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                is CreateTournamentState.Error -> {
                    Text(
                        text = (state as CreateTournamentState.Error).message,
                        color = ColorTokens.Error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(
                        onClick = { viewModel.createTournament() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Retry")
                    }
                }
                else -> {
                    Button(
                        onClick = { viewModel.createTournament() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Tournament")
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.lg))
        }
    }
}