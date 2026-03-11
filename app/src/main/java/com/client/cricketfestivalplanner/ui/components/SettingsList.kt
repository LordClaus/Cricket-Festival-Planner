package com.client.cricketfestivalplanner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.client.cricketfestivalplanner.theme.ColorTokens
import com.client.cricketfestivalplanner.theme.Dimensions

data class SettingsItem(
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false
)

@Composable
fun SettingsList(
    items: List<SettingsItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            SettingsRow(item = item)
            if (index < items.lastIndex) {
                HorizontalDivider(color = ColorTokens.Divider)
            }
        }
    }
}

@Composable
private fun SettingsRow(item: SettingsItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(Dimensions.md)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (item.isDestructive) ColorTokens.Error else ColorTokens.IconActive,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(Dimensions.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (item.isDestructive) ColorTokens.Error else ColorTokens.TextPrimary
            )
            item.subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.TextSecondary
                )
            }
        }
    }
}