package com.client.cricketfestivalplanner.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournament(tournament: TournamentEntity): Long

    @Update
    suspend fun updateTournament(tournament: TournamentEntity)

    @Delete
    suspend fun deleteTournament(tournament: TournamentEntity)

    @Query("SELECT * FROM tournaments")
    suspend fun getAllTournamentsOnce(): List<TournamentEntity>

    @Query("SELECT * FROM tournaments ORDER BY createdAt DESC")
    fun getAllTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTournaments(): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE id = :tournamentId")
    suspend fun getTournamentById(tournamentId: Int): TournamentEntity?

    @Query("DELETE FROM tournaments")
    suspend fun deleteAllTournaments()

    @Query("SELECT COUNT(*) FROM tournaments")
    suspend fun getTournamentCount(): Int
}