package com.client.cricketfestivalplanner.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import com.client.cricketfestivalplanner.ui.components.SettingsItem
import com.client.cricketfestivalplanner.ui.components.SettingsList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showClearDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when (val s = state) {
            is SettingsState.Success -> {
                snackbarHostState.showSnackbar(s.message)
                viewModel.resetState()
            }
            is SettingsState.Error -> {
                snackbarHostState.showSnackbar(s.message)
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    val settingsItems = listOf(
        SettingsItem(
            title = "Rate App",
            subtitle = "Enjoy the app? Leave a review",
            icon = Icons.Default.Star,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.client.cricketfestivalplanner"))
                context.startActivity(intent)
            }
        ),
        SettingsItem(
            title = "Share App",
            subtitle = "Share with friends",
            icon = Icons.Default.Share,
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Check out Cricket Festival Planner!")
                }
                context.startActivity(Intent.createChooser(intent, "Share"))
            }
        ),
        SettingsItem(
            title = "Privacy Policy",
            subtitle = "Read our privacy policy",
            icon = Icons.Default.Info,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://example.com/privacy"))
                context.startActivity(intent)
            }
        ),
        SettingsItem(
            title = "Clear All Data",
            subtitle = "Delete all tournaments and matches",
            icon = Icons.Default.Delete,
            onClick = { showClearDialog = true },
            isDestructive = true
        ),
        SettingsItem(
            title = "Reset Settings",
            subtitle = "Reset all preferences",
            icon = Icons.Default.Refresh,
            onClick = { showResetDialog = true },
            isDestructive = true
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(Dimensions.md)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
                    elevation = CardDefaults.cardElevation(Dimensions.cardElevation)
                ) {
                    SettingsList(items = settingsItems)
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Data") },
            text = {
                Text(
                    "Are you sure you want to delete all tournaments and matches? This cannot be undone.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllData()
                    showClearDialog = false
                }) {
                    Text("Delete", color = ColorTokens.Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Settings") },
            text = {
                Text(
                    "Are you sure you want to reset all settings?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetSettings()
                    showResetDialog = false
                }) {
                    Text("Reset", color = ColorTokens.Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}