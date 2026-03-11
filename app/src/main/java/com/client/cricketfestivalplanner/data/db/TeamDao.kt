package com.client.cricketfestivalplanner.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Update
    suspend fun updateTeam(team: TeamEntity)

    @Delete
    suspend fun deleteTeam(team: TeamEntity)

    @Query("SELECT * FROM teams WHERE tournamentId = :tournamentId ORDER BY points DESC, runDifference DESC")
    fun getTeamsByTournamentId(tournamentId: Int): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE tournamentId = :tournamentId ORDER BY points DESC, runDifference DESC")
    suspend fun getTeamsByTournamentIdOnce(tournamentId: Int): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE id = :teamId")
    suspend fun getTeamById(teamId: Int): TeamEntity?

    @Query("DELETE FROM teams WHERE tournamentId = :tournamentId")
    suspend fun deleteTeamsByTournamentId(tournamentId: Int)
}