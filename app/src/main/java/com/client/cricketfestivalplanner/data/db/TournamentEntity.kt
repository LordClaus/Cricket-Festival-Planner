package com.client.cricketfestivalplanner.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournaments")
data class TournamentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val matchType: String,
    val teamCount: Int,
    val pointSystem: String,
    val structure: String,
    val duration: String,
    val location: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)