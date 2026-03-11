package com.client.cricketfestivalplanner.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Delete
    suspend fun deleteMatch(match: MatchEntity)

    @Query("SELECT * FROM matches WHERE tournamentId = :tournamentId ORDER BY round ASC, id ASC")
    fun getMatchesByTournamentId(tournamentId: Int): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE tournamentId = :tournamentId ORDER BY round ASC, id ASC")
    suspend fun getMatchesByTournamentIdOnce(tournamentId: Int): List<MatchEntity>

    @Query("SELECT * FROM matches WHERE id = :matchId")
    suspend fun getMatchById(matchId: Int): MatchEntity?

    @Query("SELECT * FROM matches WHERE tournamentId = :tournamentId AND isCompleted = 0 ORDER BY round ASC")
    fun getPendingMatches(tournamentId: Int): Flow<List<MatchEntity>>

    @Query("DELETE FROM matches WHERE tournamentId = :tournamentId")
    suspend fun deleteMatchesByTournamentId(tournamentId: Int)

    @Query("DELETE FROM matches")
    suspend fun deleteAllMatches()
}