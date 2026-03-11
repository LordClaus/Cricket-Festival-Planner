package com.client.cricketfestivalplanner.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen2(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    val viewModel: OnboardingViewModel = koinViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = ColorTokens.PrimaryAccent,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(Dimensions.xl))

        Text(
            text = "Tournament Setup",
            style = MaterialTheme.typography.displayLarge,
            color = ColorTokens.TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.md))

        Text(
            text = "Customize your tournament format, number of teams, points system and structure to fit your needs.",
            style = MaterialTheme.typography.bodyLarge,
            color = ColorTokens.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.lg))

        Card(
            colors = CardDefaults.cardColors(containerColor = ColorTokens.InputBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(Dimensions.md)) {
                DemoRow(label = "Format", value = "T20")
                DemoRow(label = "Teams", value = "8")
                DemoRow(label = "Structure", value = "Round Robin")
                DemoRow(label = "Points", value = "Standard")
                DemoRow(label = "Duration", value = "Weekend")
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.lg))

        Text(
            text = "2 / 3",
            style = MaterialTheme.typography.bodySmall,
            color = ColorTokens.TextSecondary
        )

        Spacer(modifier = Modifier.height(Dimensions.lg))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back", color = ColorTokens.TextSecondary)
            }
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f)
            ) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.sm))

        TextButton(
            onClick = { viewModel.completeOnboarding { onSkip() } }
        ) {
            Text("Skip", color = ColorTokens.TextSecondary)
        }
    }
}

@Composable
private fun DemoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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