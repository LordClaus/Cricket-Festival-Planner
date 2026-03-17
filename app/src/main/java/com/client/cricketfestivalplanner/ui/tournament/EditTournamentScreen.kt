package com.client.cricketfestivalplanner.ui.tournament

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
fun EditTournamentScreen(
    tournamentId: Int,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: EditTournamentViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val name by viewModel.name.collectAsState()
    val matchType by viewModel.matchType.collectAsState()
    val pointSystem by viewModel.pointSystem.collectAsState()
    val structure by viewModel.structure.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val location by viewModel.location.collectAsState()
    val nameError by viewModel.nameError.collectAsState()

    var matchTypeExpanded by remember { mutableStateOf(false) }
    var pointSystemExpanded by remember { mutableStateOf(false) }
    var structureExpanded by remember { mutableStateOf(false) }
    var durationExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(tournamentId) {
        viewModel.init(tournamentId)
    }

    LaunchedEffect(state) {
        if (state is EditTournamentState.Success) {
            onSaved()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Tournament") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (state) {
            is EditTournamentState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ColorTokens.PrimaryAccent)
                }
            }
            is EditTournamentState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (state as EditTournamentState.Error).message,
                        color = ColorTokens.Error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
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
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = matchTypeExpanded)
                            },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = matchTypeExpanded,
                            onDismissRequest = { matchTypeExpanded = false }
                        ) {
                            viewModel.matchTypes.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onMatchTypeChange(option)
                                        matchTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = pointSystemExpanded,
                        onExpandedChange = { pointSystemExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = pointSystem,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Point System") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = pointSystemExpanded)
                            },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = pointSystemExpanded,
                            onDismissRequest = { pointSystemExpanded = false }
                        ) {
                            viewModel.pointSystems.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onPointSystemChange(option)
                                        pointSystemExpanded = false
                                    }
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
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = structureExpanded)
                            },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = structureExpanded,
                            onDismissRequest = { structureExpanded = false }
                        ) {
                            viewModel.structures.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onStructureChange(option)
                                        structureExpanded = false
                                    }
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
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded)
                            },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = durationExpanded,
                            onDismissRequest = { durationExpanded = false }
                        ) {
                            viewModel.durations.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onDurationChange(option)
                                        durationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = location,
                        onValueChange = { if (it.length <= 50) viewModel.onLocationChange(it) },
                        label = { Text("Location (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.sm))

                    Button(
                        onClick = { viewModel.saveChanges() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Changes")
                    }

                    Spacer(modifier = Modifier.height(Dimensions.lg))
                }
            }
        }
    }
}