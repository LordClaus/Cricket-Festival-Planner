package com.client.cricketfestivalplanner.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "matches",
    foreignKeys = [
        ForeignKey(
            entity = TournamentEntity::class,
            parentColumns = ["id"],
            childColumns = ["tournamentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tournamentId"])]
)
data class MatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tournamentId: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeTeamName: String,
    val awayTeamName: String,
    val matchTime: String,
    val isCompleted: Boolean = false,
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val bestPlayerId: Int? = null,
    val bestPlayerName: String? = null,
    val round: Int = 1
)