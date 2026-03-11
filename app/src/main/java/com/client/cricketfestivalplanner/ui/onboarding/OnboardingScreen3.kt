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
import androidx.compose.material.icons.filled.BarChart
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
fun OnboardingScreen3(
    onFinish: () -> Unit,
    onBack: () -> Unit
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
            imageVector = Icons.Default.BarChart,
            contentDescription = null,
            tint = ColorTokens.PrimaryAccent,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(Dimensions.xl))

        Text(
            text = "Track & Analyze",
            style = MaterialTheme.typography.displayLarge,
            color = ColorTokens.TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.md))

        Text(
            text = "Track match results, view tournament standings, analyze team performance and keep a full history of all events.",
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
                StatRow(label = "Tournaments", value = "12")
                StatRow(label = "Matches Played", value = "48")
                StatRow(label = "Top Team", value = "Mumbai Lions")
                StatRow(label = "Best Player", value = "R. Sharma")
                StatRow(label = "History", value = "Full Archive")
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.lg))

        Text(
            text = "3 / 3",
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
                onClick = { viewModel.completeOnboarding { onFinish() } },
                modifier = Modifier.weight(1f)
            ) {
                Text("Finish")
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
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