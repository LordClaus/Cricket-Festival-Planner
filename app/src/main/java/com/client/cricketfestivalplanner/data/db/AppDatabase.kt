package com.client.cricketfestivalplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TournamentEntity::class, TeamEntity::class, MatchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tournamentDao(): TournamentDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao

    companion object {
        const val DATABASE_NAME = "cricket_festival_planner.db"
    }
}