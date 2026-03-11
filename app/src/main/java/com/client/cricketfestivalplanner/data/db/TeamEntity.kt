package com.client.cricketfestivalplanner.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "teams",
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
data class TeamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tournamentId: Int,
    val name: String,
    val points: Int = 0,
    val matchesPlayed: Int = 0,
    val matchesWon: Int = 0,
    val matchesLost: Int = 0,
    val runDifference: Int = 0
)