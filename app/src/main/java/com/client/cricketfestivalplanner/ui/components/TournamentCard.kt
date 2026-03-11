package com.client.cricketfestivalplanner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions
import com.client.cricketfestivalplanner.utils.DateFormatter

@Composable
fun TournamentCard(
    tournament: TournamentEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = ColorTokens.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.cardElevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.md)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${tournament.teamCount} teams • ${tournament.structure}",
                style = MaterialTheme.typography.bodyLarge,
                color = ColorTokens.TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = DateFormatter.formatForDisplay(
                    DateFormatter.formatForStorage(
                        java.util.Date(tournament.createdAt)
                    )
                ),
                style = MaterialTheme.typography.bodySmall,
                color = ColorTokens.TextSecondary
            )
        }
    }
}