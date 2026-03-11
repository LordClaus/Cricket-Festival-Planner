package com.client.cricketfestivalplanner.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
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
fun OnboardingScreen1(
    onNext: () -> Unit,
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
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = ColorTokens.PrimaryAccent,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(Dimensions.xl))

        Text(
            text = "Cricket Festival Planner",
            style = MaterialTheme.typography.displayLarge,
            color = ColorTokens.TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.md))

        Text(
            text = "Organize and manage local cricket tournaments with ease. Perfect for schools, clubs and community leagues.",
            style = MaterialTheme.typography.bodyLarge,
            color = ColorTokens.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.xl))

        Text(
            text = "1 / 3",
            style = MaterialTheme.typography.bodySmall,
            color = ColorTokens.TextSecondary
        )

        Spacer(modifier = Modifier.height(Dimensions.lg))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }

        Spacer(modifier = Modifier.height(Dimensions.sm))

        TextButton(
            onClick = { viewModel.completeOnboarding { onSkip() } }
        ) {
            Text("Skip", color = ColorTokens.TextSecondary)
        }
    }
}